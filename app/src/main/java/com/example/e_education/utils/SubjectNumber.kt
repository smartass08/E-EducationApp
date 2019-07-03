package com.example.e_education.utils

object SubjectNumber {
    const val Physics = 1
    const val Chemistry = 2
    const val Bio = 3

    fun toKey(subject: String): Int{
        val lwrSubject = subject.toLowerCase()
        if (lwrSubject == "physics")
            return Physics
        if (lwrSubject == "chemistry")
            return Chemistry
        if (lwrSubject == "biology" || lwrSubject == "bio")
            return Bio
        return -1
    }

    fun toString(key: Int): String{
        return when (key){
            Physics -> "Physics"
            Chemistry -> "Chemistry"
            Bio -> "Bio"
            else -> ""
        }
    }
}