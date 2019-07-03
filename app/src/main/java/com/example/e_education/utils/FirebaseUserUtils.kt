package com.example.e_education.utils

import com.example.e_education.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

fun getAppUser(auth: FirebaseAuth, db: FirebaseFirestore, uid: String) {
    val currUser = auth.currentUser
    if (currUser != null) {
        val docRef = db.collection(User.USER_FIELD_NAME).document(currUser.uid)
        docRef.get().addOnSuccessListener {
            if (!it.exists()) {

            }
        }
    }
}