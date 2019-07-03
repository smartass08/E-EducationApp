package com.example.e_education

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import android.content.Intent
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.annotation.UiThread
import com.example.e_education.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.jakewharton.rxbinding2.widget.RxTextView
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers

import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import java.util.concurrent.TimeUnit

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity(), CoroutineScope by MainScope() {

    private val db = FirebaseFirestore.getInstance()
    var focusView: View? = null
    private val auth = FirebaseAuth.getInstance()

    override fun onStart() {
        super.onStart()
        val currUser = auth.currentUser
        if (currUser != null){
            showProgress(true)
            val docRef = db.collection(User.USER_FIELD_NAME).document(currUser.uid)
            docRef.get().addOnSuccessListener {
                finish()
                val intent: Intent
                if (!it.exists()){

                    // profile is not complete
                    intent = Intent(this@LoginActivity, NewUserInfoCollectionActivity::class.java)
                    intent.putExtra(EMAIL_INTENT, currUser.email)
                    intent.putExtra(UID_INTENT, currUser.uid)
                    startActivity(intent)
                } else {
                    val user = it.toObject(User::class.java)
                    // everything is fine. launch MainActivity
                    Log.d(TAG, "${user!!.email}: ${user.isVerified}, ${user.formNum}, ${user.standard}")
                    intent = Intent(this@LoginActivity, MainActivity::class.java)
                    startActivity(intent)
                }
            }

                }
            }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        password.setOnEditorActionListener(TextView.OnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_DONE || id == EditorInfo.IME_NULL) {
                attemptLogin()
                return@OnEditorActionListener true
            }
            false
        })

        RxTextView.afterTextChangeEvents(email)
            .skipInitialValue()
            .map {
                email.error = null
                it.view().text.toString()
            }
            .debounce(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
            .compose(verifyEmailPattern)
            .compose(verifyEmailPattern)
            .compose(retryWhenError{
                email.error = it.message
            })
            .subscribe()

        RxTextView.afterTextChangeEvents(password)
            .skipInitialValue()
            .map {
                password.error = null
                it.view().text.toString()
            }
            .debounce(500, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())
            .compose(verifyPasswordLength)
            .compose(verifyPasswordLength)
            .compose(retryWhenError{
                password.error = it.message
            })
            .subscribe()

        email_sign_in_button.setOnClickListener { attemptLogin() }
        email_register_button.setOnClickListener{ attemptSignUp()}
    }


    override fun onDestroy() {
        super.onDestroy()
        cancel()
    }

    private val verifyEmailPattern = ObservableTransformer<String, String> { observable ->
        observable.flatMap {
            Observable.just(it).map { it.trim() } //removing white spaces
                .filter {
                    Patterns.EMAIL_ADDRESS.matcher(it).matches() // setting the email pattern - abc@def.com -
                }
                .singleOrError()
                .onErrorResumeNext {
                    if (it is NoSuchElementException) {
                        Single.error(Exception("Invalid email format")) //informing the usher
                    } else {
                        Single.error(it)
                    }

                }.toObservable()
        }
    }


    private inline fun retryWhenError(crossinline onError: (ex: Throwable) -> Unit): ObservableTransformer<String, String> = ObservableTransformer { observable ->
        observable.retryWhen { errors ->
            errors.flatMap {
                onError(it)
                Observable.just("")
            }
        }


    }
    private val verifyPasswordLength = ObservableTransformer<String, String> { observable ->
        observable.flatMap {
            Observable.just(it).map { it.trim() } //removing white spaces on the string
                .filter { it.length > 8 } //setting the length to 8
                .singleOrError()
                .onErrorResumeNext {
                    if (it is NoSuchElementException) {
                        Single.error(Exception("Password must be at least 8 characters")) //informing the user
                    } else {
                        Single.error(it)
                    }
                }
                .toObservable()
        }
    }

    private fun isFormValid(): Boolean{
        return (email.error == null && password.error == null)
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private fun attemptLogin() {
        if (isFormValid()) {
            showProgress(true)
            loginUser(email.text.toString(), password.text.toString())
        } else{
                focusView?.requestFocus()
        }
    }
    private fun attemptSignUp(){
        if (isFormValid()) {
            showProgress(true)
            createNewUser(email.text.toString(), password.text.toString())
        } else{
            focusView?.requestFocus()
        }
    }

    /**
     * Shows the progress UI and hides the login form.
     */
    private fun showProgress(show: Boolean) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
            val shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime).toLong()

            login_form.visibility = if (show) View.GONE else View.VISIBLE
            login_form.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 0 else 1).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_form.visibility = if (show) View.GONE else View.VISIBLE
                    }
                })

            login_progress.visibility = if (show) View.VISIBLE else View.GONE
            login_progress.animate()
                .setDuration(shortAnimTime)
                .alpha((if (show) 1 else 0).toFloat())
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        login_progress.visibility = if (show) View.VISIBLE else View.GONE
                    }
                })
    }

    private fun createNewUser(mEmail: String, mPassword: String){
        auth.createUserWithEmailAndPassword(mEmail, mPassword)
            .addOnCompleteListener(this){
                showProgress(it.isSuccessful)
                if (it.isSuccessful)
                {
                    Log.d(TAG, "Register success")
                    val user = auth.currentUser
                    finish()
                    val intent = Intent(this, NewUserInfoCollectionActivity::class.java)
                    intent.putExtra(UID_INTENT, user!!.uid)
                    intent.putExtra("email", user.email)
                    startActivity(intent)
                } else {
                    Log.d(TAG, "An error occurred")
                }
            }
    }
    @UiThread
    private fun loginUser(mEmail: String, mPassword: String) {
        auth.signInWithEmailAndPassword(mEmail, mPassword)
            .addOnCompleteListener (this){
                showProgress(it.isSuccessful)
                if (it.isSuccessful){
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this, "Could not login. Some error occurred!", Toast.LENGTH_LONG).show()
                }
            }
    }

    companion object {
        private const val UID_INTENT = "uid"
        private const val EMAIL_INTENT = "email"
        private const val TAG = "LoginActivity"
    }
}
