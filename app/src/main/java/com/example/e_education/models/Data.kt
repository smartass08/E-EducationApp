package com.example.e_education.models

import android.graphics.Bitmap


// This file defines all the data classes for the ViewModel classes of the application

data class ContinueWatchingData(
    val id: Int,
    val image: Int,
    val caption: String
)

data class SliderData(
    val id: Int,
    val image: Int
    /*, val caption: String*/
)

data class Lecture(
    var id: String = "",
    var lectureName: String = "",
    var ofChapter: Int = -1,
    var chapterName: String = "",
    var standard: String = "",
    var subject: Int = -1,
    var imgUrl: String = ""
){
    fun isValid(): Boolean =
            ofChapter != -1 &&
            id.isNotEmpty() &&
            chapterName.isNotEmpty() &&
            standard.isNotEmpty() &&
            subject != -1 &&
            lectureName.isNotEmpty()
}

data class User(
    val name: String,
    val email: String,
    val standard: String,
    val formNum: String,
    val isVerified: Boolean
){
    constructor() : this("", "", "", "", false)
    companion object {
        const val USER_FIELD_NAME = "users"
    }
}