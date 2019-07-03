package com.example.e_education.models

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.e_education.models.repository.ChapterRepository
import com.example.e_education.utils.SubjectNumber

class ChaptersViewModel: ViewModel(){

    private val TAG = "ChaptersViewModel"
    private var chapterData : MutableLiveData<List<Chapter>>? = MutableLiveData()
    private var filteredChapterData : MutableLiveData<List<Chapter>>? = MutableLiveData()
    private val chapterRepos = ChapterRepository()
    private var subject: Int? = null
    private var standard: String? = null

    init {
        Log.d("log", "created")
    }
    fun init(standard: String, subject: Int){
        this.subject = subject
        this.standard = standard
        chapterData = getChapterList()
    }
    fun getChapterList(): MutableLiveData<List<Chapter>>? {
        chapterRepos.getAllChapters("XII", SubjectNumber.Physics).addSnapshotListener{ value, e ->
            if (e != null){
                filteredChapterData?.value = null
                chapterData?.value = null
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            val savedList = mutableListOf<Chapter>()
            for (doc in value!!){
                val addItem = doc.toObject(Lecture::class.java)
                savedList.add(Chapter(SubjectNumber.toString(addItem.subject), addItem.ofChapter, addItem.chapterName, addItem.standard))
            }
            filteredChapterData?.value = savedList
        }
        return filteredChapterData
    }


    fun search(query: String?) {
        if (query.isNullOrEmpty()){
            filteredChapterData?.value = chapterData?.value
        }
        else {
            filteredChapterData?.value = chapterData?.value?.filter {
                it.chapterName.toLowerCase().contains(query.toLowerCase())
            }?.toList()
        }
    }

    fun insert(chapter: Chapter, lecture: Lecture) {
        chapterRepos.insert(lecture)
    }
    fun delete(chapter: Chapter) = chapterRepos.delete(chapter)
    fun update(chapter: Chapter) = chapterRepos.update(chapter)
}