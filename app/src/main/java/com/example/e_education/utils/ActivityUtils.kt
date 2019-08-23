package com.example.e_education.utils

import android.content.Context
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.example.e_education.R
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

object ActivityIndex{
    const val MainActivity = 0
    const val LectureActivity = 1
    const val ChaptersActivity = 2
}

const val TAG = "ActivityUtils"
fun Context.toast(message: String, duration: Int = Toast.LENGTH_LONG){
    Toast.makeText(this, message, duration).show()
}

fun ImageView.load(imgRef: String) {
    Log.d(TAG, imgRef)
    val ref = FirebaseStorage.getInstance().reference
        .child(imgRef)
    ref.downloadUrl.addOnSuccessListener {
        Log.d(TAG, it.path)
        Picasso.get()
            .load(it)
            .error(R.drawable.physics)
            .into(this)
    }
}