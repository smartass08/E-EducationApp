package com.example.e_education.models


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
    var imgRef: String = ""
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

class IntentData(
    val user: User,
    val subject: Int,
    val activityIndex: Int
) {
    constructor() : this(User(), -1, -1)
    constructor(data: IntentData) : this(data.user, data.subject, data.activityIndex)
    constructor(data: IntentData, activityIndex: Int) : this(data.user, data.subject, activityIndex)

    companion object {
        const val name = "intent-data"
    }
}