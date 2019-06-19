package com.example.e_education.viewmodel

import android.content.res.Resources
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.e_education.viewmodel.repository.LectureRepository

class LectureViewModel: ViewModel() {
    private lateinit var repo: LectureRepository
    private var lectureList =  MutableLiveData<List<LectureData>>()
    private val filteredLectureList = MutableLiveData<List<LectureData>>()

    fun init(resources: Resources){
        // Must call to properly instantiate
        repo = LectureRepository(resources)
        lectureList = repo.getAllLectures()
        filteredLectureList.value = lectureList.value
    }
    fun getLectureList(): LiveData<List<LectureData>>{
        return filteredLectureList
    }

    fun search(p0: String?){
        if (p0.isNullOrEmpty())
            filteredLectureList.value = lectureList.value
        else{
            filteredLectureList.value = lectureList.value?.filter {
             it.lectureName.toLowerCase().contains(p0.toLowerCase())
            }?.toList()
        }
    }
}