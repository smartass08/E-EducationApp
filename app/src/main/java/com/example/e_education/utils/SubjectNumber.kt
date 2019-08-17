package com.example.e_education.utils

object SubjectNumber {
    const val Physics = 1
    const val Chemistry = 2
    const val Bio = 3
    const val Maths = 4
    const val Notes = 5
    const val QuesPaper = 6

    fun toKey(subject: String): Int{
        val lwrSubject = subject.toLowerCase()
        if (lwrSubject == "maths" || lwrSubject == "math")
            return Maths
        if (lwrSubject == "physics")
            return Physics
        if (lwrSubject == "chemistry")
            return Chemistry
        if (lwrSubject == "biology" || lwrSubject == "bio")
            return Bio
        return when(lwrSubject){
            "maths" -> Maths
            "math" -> Maths
            "physics" -> Physics
            "chemistry" -> Chemistry
            "biology" -> Bio
            "notes" -> Notes
            "question papers" -> QuesPaper
            else -> -1
        }
    }

    fun toString(key: Int): String{
        return when (key){
            Maths -> "Maths"
            Physics -> "Physics"
            Chemistry -> "Chemistry"
            Bio -> "Bio"
            Notes -> "Notes"
            QuesPaper -> "Question Papers"
            else -> ""
        }
    }
}