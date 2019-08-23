package com.example.e_education.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.e_education.models.repository.ChapterRepository
import com.example.e_education.utils.Coroutines
import com.example.e_education.utils.SubjectNumber
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

interface UploadListener {
    fun onUploadStarted()
    fun onUploadComplete()
    fun onUploadFailed(msg: String)
}
class ChaptersViewModel: ViewModel(){

    private val TAG = "ChaptersViewModel"
    private var chapterData : MutableLiveData<List<Lecture>>? = MutableLiveData()
    private var filteredChapterData : MutableLiveData<List<Lecture>>? = MutableLiveData()
    val chapterRepos = ChapterRepository()
    val uploadProgress = chapterRepos.uplaodProgress
    private var subject: String? = null
    private var standard: String? = null
    val auth = FirebaseAuth.getInstance()
    val authUser = auth.currentUser
    val db = FirebaseFirestore.getInstance()
    private var chapter: Int = -1

    val currUser = object: LiveData<User>() {
        override fun onActive() {
            super.onActive()

                val docRef = db.collection(User.USER_FIELD_NAME).document(authUser!!.uid)
                docRef.get().addOnSuccessListener {
                    value = it.toObject(User::class.java)

                }
            }
        }

    init {
        Log.d("log", "created")
    }
    fun init(standard: String, subject: Int, chapterNumber: Int = -1){
        this.subject = SubjectNumber.toString(subject)
        this.standard = standard
        this.chapter = chapterNumber
        chapterData?.value = getChapterList()?.value
    }
    fun getChapterList(): MutableLiveData<List<Lecture>>? {
        chapterRepos.getAllChapters(standard!!, subject!!, chapter).addSnapshotListener{ value, e ->
            if (e != null){
                filteredChapterData?.value = null
                chapterData?.value = null
                Log.w(TAG, "Listen failed.", e)
                return@addSnapshotListener
            }
            val savedList = mutableListOf<Lecture>()
            for (doc in value!!){
                val addItem = doc.toObject(Lecture::class.java)
                savedList.add(addItem)
            }
            chapterData?.value = savedList
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

    fun insert(lecture: Lecture, lectureImage: ByteArray) {
        Coroutines.main { chapterRepos.insert(lecture, lectureImage) }
    }
    fun delete(chapter: Lecture) =  Coroutines.main { chapterRepos.delete(chapter) }
    fun update(chapter: Lecture) = Coroutines.main { chapterRepos.update(chapter) }
}