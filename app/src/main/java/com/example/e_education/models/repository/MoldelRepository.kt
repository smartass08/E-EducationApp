package com.example.e_education.models.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.e_education.models.Lecture
import com.example.e_education.models.UploadListener
import com.example.e_education.utils.SubjectNumber
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class ChapterRepository{

    companion object {
        const val collectionRoot = "chapters"
    }

    val uplaodProgress: MutableLiveData<Long> = MutableLiveData()
    private val TAG = "ChapterRepository"
    private val db = FirebaseFirestore.getInstance()
    private val collectionRef = db.collection(collectionRoot)
    private val storageRef = FirebaseStorage.getInstance().reference
    var uploadListener: UploadListener? = null
    private suspend fun insertInBackground(lecture: Lecture, imageData: ByteArray) {
        val fileExtension = "jpg"
        lecture.imgRef = "${lecture.ofChapter}/${UUID.randomUUID()}.$fileExtension"
        val ref = storageRef.child(lecture.imgRef)
        val uploadTask = ref.putBytes(imageData)
            .addOnProgressListener {
                uplaodProgress.value = (100 * it.bytesTransferred) / it.totalByteCount
            }
            uploadTask.addOnSuccessListener {
                Log.d(TAG, it.storage.downloadUrl.toString())
                db.collection(collectionRoot).document()
                    .set(lecture)
                    .addOnSuccessListener {
                        uploadListener!!.onUploadComplete()
                    }
                    .addOnFailureListener {
                        uploadListener!!.onUploadFailed("Failure adding Lecture: $it")
                    }
            }.addOnFailureListener {
                Log.d(TAG, it.message)
            }
    }

    private suspend fun updateInBackground(lecture: Lecture){
        db.collection(collectionRoot).document("${lecture.ofChapter}")
            .get()
            .addOnSuccessListener {
                if (it.exists()) {
                    collectionRef.document("${lecture.ofChapter}")
                        .set(lecture)
                        .addOnSuccessListener {
                            Log.i(TAG, "Lecture Updated")
                        }
                } else {
                    Log.i(TAG, "Lecture doesn't exists")
                }
            }
    }
    private suspend fun deleteInBackground(lecture: Lecture){
        db.collection(collectionRoot).document("${lecture.ofChapter}")
            .delete()
    }

    suspend fun insert(lecture: Lecture, lectureImage: ByteArray) {
        uploadListener!!.onUploadStarted()
        if (lecture.isValid())
            insertInBackground(lecture, lectureImage)
        else
            uploadListener!!.onUploadFailed("Invalid input! Please check your input properly")
    }

    suspend fun delete(lecture: Lecture) {
        if (lecture.isValid())
            deleteInBackground(lecture)
    }

    suspend fun update(lecture: Lecture) {
        if (lecture.isValid()) {
            updateInBackground(lecture)
        }
    }

    fun getAllChapters(standard: String, subject: String, chapterNumber: Int): Query{
        val subjectKey = SubjectNumber.toKey(subject)
        Log.d(TAG, "$standard: $subjectKey: $subject")
        return if (chapterNumber == -1) {
            db.collection(collectionRoot)
                .whereEqualTo("standard", standard)
                .whereEqualTo("subject", subjectKey)
        } else {
            db.collection(collectionRoot)
                .whereEqualTo("standard", standard)
                .whereEqualTo("subject", subjectKey)
                .whereEqualTo("ofChapter", chapterNumber)
        }
    }
}