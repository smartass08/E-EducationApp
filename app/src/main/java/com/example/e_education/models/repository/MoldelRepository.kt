package com.example.e_education.models.repository

import android.net.Uri
import android.util.Log
import com.example.e_education.models.Lecture
import com.example.e_education.utils.SubjectNumber
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageMetadata
import java.io.File
import java.util.UUID

class ChapterRepository{

    companion object {
        const val collectionRoot = "chapters"
        const val lecturePath = "lectures"
    }
    private val TAG = "ChapterRepository"
    private val db = FirebaseFirestore.getInstance()
    private val collectionRef = db.collection(collectionRoot)
    private val storageRef = FirebaseStorage.getInstance().reference

    private suspend fun insertInBackground(lecture: Lecture, lectureImage: Uri?){
        if (lectureImage != null){
            val file = Uri.fromFile(File(lectureImage.path))
            val metadata = StorageMetadata.Builder()
                .setContentType("image/jpeg")
                .build()
            val ref = storageRef.child("${lecture.ofChapter}/${UUID.randomUUID()}.jpg")
            val uploadTask = ref.putFile(file, metadata)
            uploadTask.addOnSuccessListener {
                Log.d(TAG, it.storage.downloadUrl.toString())
            }.addOnFailureListener {
                Log.d(TAG, it.message)
            }
        }

        db.collection(collectionRoot).document()
             .set(lecture)
             .addOnSuccessListener {
                 Log.d("ChapterRepository", "Successfully added lecture")
                }
             .addOnFailureListener {
                    Log.d("ChapterRepository", "Failure adding Lecture: $it")
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
    suspend fun insert(lecture: Lecture, lectureImage: Uri?) {
        if (lecture.isValid())
            insertInBackground(lecture, lectureImage)
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