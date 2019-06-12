package com.example.e_education.viewmodel

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
    val id: Int, // primary key
    val chapterName: String?
)

data class LectureData(
    val id: Int,
    val lectureName: String,
    val lectureImage: Bitmap
)
