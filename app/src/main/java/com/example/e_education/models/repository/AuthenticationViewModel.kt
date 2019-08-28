package com.example.e_education.models.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.e_education.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class AuthenticationViewModel(val listener: AuthListener) : ViewModel() {
    interface AuthListener {
        fun onLoginSuccess()
        fun onLoginFailure(msg: String)
        fun onProfileInComplete()
    }

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    var user: MutableLiveData<User> = MutableLiveData()

    init {
        getUser()
    }

    fun getUser(): LiveData<User> {
        user.value = null
        if (auth.currentUser != null) {
            val docRef = db.collection(User.USER_FIELD_NAME).document(auth.currentUser?.uid!!)
            docRef.get().addOnSuccessListener {
                if (it.exists()) {
                    // Profile complete. Start MainActivity
                    user.value = it.toObject(User::class.java)
                } else {
                    listener.onProfileInComplete()
                }
            }
        }
        return user
    }

    fun isLoggedIn(): Boolean = (auth.currentUser != null)

    fun getUserEmail(): String? = auth.currentUser?.email
    fun getUserUID(): String? = auth.currentUser?.uid
    fun isProfileComplete(): Boolean = true // TODO: Implement the actual logic

}