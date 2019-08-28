package com.example.e_education

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.e_education.models.AuthenticationViewModel
import com.example.e_education.models.AuthenticationViewModel.AuthListener
import com.example.e_education.models.IntentData
import com.example.e_education.models.User
import com.example.e_education.models.factory.AuthViewModelFactory
import com.example.e_education.utils.ActivityIndex
import com.example.e_education.utils.putExtra
import com.example.e_education.utils.toast
import com.firebase.ui.auth.AuthUI

/**
 * A login screen that offers login via email/password.
 */
class LoginActivity : AppCompatActivity(), AuthListener {

    private var viewModel: AuthenticationViewModel? = null
    private val factory = AuthViewModelFactory(this)
    private var user: User? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, factory).get(AuthenticationViewModel::class.java)
        viewModel?.user?.observe(this, Observer {
            if (it != null) {
                val intent = Intent(this@LoginActivity, MainActivity::class.java)
                val data = IntentData(it, -1, ActivityIndex.LoginActivity)
                intent.putExtra(IntentData.name, data)
                startActivity(intent)
                finish()
            }
        })
        if (!viewModel!!.isLoggedIn()) {
            val loginIntent = AuthUI.getInstance().createSignInIntentBuilder()
                .setAvailableProviders(
                    arrayListOf(
                        AuthUI.IdpConfig.EmailBuilder().build(),
                        AuthUI.IdpConfig.GoogleBuilder().build()
                    )
                )
                .build()
            startActivityForResult(loginIntent, RC_LOGIN)
        } else {
            onLoginSuccess()
        }
    }

    override fun onLoginSuccess() {
        toast("Login Success")
        viewModel?.getUser()
    }

    override fun onLoginFailure(msg: String) {
        toast(msg)
    }

    override fun onProfileInComplete() {
        // profile is not complete
        intent = Intent(this@LoginActivity, NewUserInfoCollectionActivity::class.java)
        intent.putExtra(EMAIL_INTENT, viewModel?.getUserEmail())
        intent.putExtra(UID_INTENT, viewModel?.getUserUID())
        startActivity(intent)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_LOGIN) {
            if (resultCode == Activity.RESULT_OK)
                onLoginSuccess()
            else if (resultCode == Activity.RESULT_CANCELED) {
                finish()
            }
        }
    }


    companion object {
        private const val UID_INTENT = "uid"
        private const val EMAIL_INTENT = "email"
        private const val TAG = "LoginActivity"
        private const val RC_LOGIN = 1
    }
}
