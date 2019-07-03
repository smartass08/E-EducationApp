package com.example.e_education.models.DAO

import androidx.lifecycle.MutableLiveData
import com.example.e_education.models.Chapter
import com.example.e_education.models.Lecture
import com.example.e_education.models.LectureData
import javax.security.auth.Subject

interface ChapterDAO {
    fun insert(lecture: Lecture)
    fun delete(chapter: Chapter)
    fun update(chapter: Chapter)
    fun getAllChapters(standard: String, subject: Int)
}

interface LectureDAO {
    fun insert(lecture: LectureData)
    fun delete(lecture: LectureData)
    fun update(lecture: LectureData)
    fun getAllLectures(): MutableLiveData<List<LectureData>>
}