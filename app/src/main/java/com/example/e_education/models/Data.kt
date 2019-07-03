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

data class Chapter (
    var subject: String,
    var chapterNumber: Int,
    var chapterName: String,
    var standard: String
){
    constructor(): this("",0, "", "")
    companion object {
        const val DB_FIELD_NAME = "chapters"
    }
}

data class LectureData(
    val id: String,
    val lectureName: String,
    val lectureImage: Bitmap
)
data class Lecture(
    val id: String = "",
    val lectureName: String = "",
    val ofChapter: Int = -1,
    val chapterName: String = "",
    val standard: String = "",
    val subject: Int = -1
)

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