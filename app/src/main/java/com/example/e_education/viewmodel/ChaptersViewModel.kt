package com.example.e_education.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.e_education.viewmodel.repository.ChapterRepository

class ChaptersViewModel: ViewModel(){

    private var chapterData = MutableLiveData<List<Chapter>>()
    private var filteredChapterData = MutableLiveData<List<Chapter>>()
    private val chapterRepos = ChapterRepository()

    init {
        Log.d("log", "created")
        chapterData = chapterRepos.getAllChapters()!!
        filteredChapterData.value = chapterData.value
    }
    fun getChapterList(): LiveData<List<Chapter>>? {
        Log.d("g", chapterRepos.getAllChapters().toString())
        return filteredChapterData
    }

    fun search(querry: String?) {
        if (querry.isNullOrEmpty()){
            filteredChapterData.value = chapterData.value
        }
        else {
            filteredChapterData.value = chapterData.value?.filter {
                it.chapterName?.toLowerCase()?.contains(querry.toLowerCase())!!
            }?.toList()
        }
    }

    fun insert(chapter: Chapter) = chapterRepos.insert(chapter)
    fun delete(chapter: Chapter) = chapterRepos.delete(chapter)
    fun update(chapter: Chapter) = chapterRepos.update(chapter)
}