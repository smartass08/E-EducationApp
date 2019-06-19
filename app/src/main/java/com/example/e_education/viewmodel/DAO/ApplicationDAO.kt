package com.example.e_education.viewmodel.DAO

import androidx.lifecycle.MutableLiveData
import com.example.e_education.viewmodel.Chapter
import com.example.e_education.viewmodel.LectureData

interface ChapterDAO {
    fun insert(chapter: Chapter)
    fun delete(chapter: Chapter)
    fun update(chapter: Chapter)
    fun getAllChapters(): MutableLiveData<List<Chapter>>?
}

interface LectureDAO {
    fun insert(lecture: LectureData)
    fun delete(lecture: LectureData)
    fun update(lecture: LectureData)
    fun getAllLectures(): MutableLiveData<List<LectureData>>
}