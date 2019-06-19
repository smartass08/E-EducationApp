package com.example.e_education

import android.content.Intent
import android.os.AsyncTask
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity;
import com.example.e_education.viewmodel.User
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*

import kotlinx.android.synthetic.main.activity_user_info.*

class NewUserInfoCollectionActivity : AppCompatActivity() {


    private var mUpdateTask: UpdateTask? = null
    private lateinit var fullNameStr: String
    private lateinit var formStr: String
    private var focusView: View? = null
    private val dbRef = FirebaseFirestore.getInstance()
    private val classSupported = arrayListOf("Select Class", "XII", "XI")
    private var uid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_info)
        setSupportActionBar(toolbar)
        title = "Getting some more info"

        classSpinner.adapter = ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_dropdown_item,
            classSupported)
        submit_button.setOnClickListener{ updateDetailsToDB() }

        uid = intent.getStringExtra("uid")
    }

    private fun updateDetailsToDB(){
        if (mUpdateTask != null) {
            return
        }

        // Reset errors.
        fullName.error = null
        formInput.error = null

        // Store values at the time of the login attempt.
        fullNameStr = fullName.text.toString()
        formStr = formInput.text.toString()

        var cancel = false

        // Check for a valid email address.
        if (TextUtils.isEmpty(fullNameStr)) {
            fullName.error = getString(R.string.error_field_required)
            focusView = fullName
            cancel = true
        }  else if (TextUtils.isEmpty(formStr)){
            formInput.error = getString(R.string.error_field_required)
            focusView = formInput
            cancel = true
        }  else if (classSpinner.selectedItemPosition == 0){
            Toast.makeText(this, "Class not selected", Toast.LENGTH_LONG).show()
            cancel = true
        }
        if (cancel){
            focusView?.requestFocus()
        } else {
            val user = User(fullNameStr, intent.getStringExtra("email"), classSupported[classSpinner.selectedItemPosition], formStr, true)
            mUpdateTask = UpdateTask(user)
            mUpdateTask!!.execute(null)
        }

    }

    inner class UpdateTask(val user: User) : AsyncTask<Void, Void, Unit>(){
        override fun doInBackground(vararg p0: Void?){
            dbRef.collection(User.USERS_DB_NAME).document(uid!!)
                .set(user)
                .addOnSuccessListener{
                    Log.d("UpdateTask", "success")
                    finish()
                    val intent = Intent(baseContext, MainActivity::class.java)
                    startActivity(intent)
                }
                .addOnFailureListener{
                    Log.d("UpdateTask", it.toString())
                }
        }

    }

}
