package com.example.e_education.utils

import android.content.Context
import android.content.Intent
import android.widget.Toast
import com.example.e_education.MainActivity

object ActivityIndex{
    const val MainActivity = 0
    const val LectureActivity = 1
    const val ChaptersActivity = 2
}
fun Context.toast(message: String, duration: Int = Toast.LENGTH_LONG){
    Toast.makeText(this, message, duration).show()
}