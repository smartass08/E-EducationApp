package com.example.e_education.utils

import android.graphics.Bitmap

data class LectureData(val lectureName: String, val lectureImage: Bitmap)

data class ContinueWatchingData(val image: Int, val caption: String)

data class SliderData(val image: Int/*, val caption: String*/)
