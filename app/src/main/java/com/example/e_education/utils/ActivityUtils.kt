package com.example.e_education.utils

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import com.example.e_education.R
import com.google.firebase.storage.FirebaseStorage
import com.google.gson.Gson
import com.squareup.picasso.Callback
import com.squareup.picasso.NetworkPolicy
import com.squareup.picasso.Picasso

object ActivityIndex{
    const val MainActivity = 0
    const val LectureActivity = 1
    const val ChaptersActivity = 2
    const val LoginActivity = 3
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
        val p = Picasso.get()
        p.setIndicatorsEnabled(true)
        p.load(it)
            .error(R.drawable.physics)
            .networkPolicy(NetworkPolicy.OFFLINE)
            .into(this, object : Callback {
                override fun onSuccess() {}

                override fun onError(p0: Exception?) {
                    Picasso.get()
                        .load(it)
                        .error(R.drawable.physics)
                        .into(this@load)
                }

            })
    }
}

fun Intent.putExtra(name: String, src: Any) {
    val gson = Gson()
    this.putExtra(name, gson.toJson(src))
}

fun <T : Any?> Intent.getExtra(name: String, to: Class<T>): T {
    val gson = Gson()
    return (gson.fromJson(this.getStringExtra(name), to))
}