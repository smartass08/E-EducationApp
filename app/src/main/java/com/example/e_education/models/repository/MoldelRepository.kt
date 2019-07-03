package com.example.e_education.models.repository

import android.content.res.Resources
import android.graphics.BitmapFactory
import android.os.AsyncTask
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.e_education.R
import com.example.e_education.models.Chapter
import com.example.e_education.models.DAO.ChapterDAO
import com.example.e_education.models.DAO.LectureDAO
import com.example.e_education.models.Lecture
import com.example.e_education.models.LectureData
import com.example.e_education.utils.SubjectNumber
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import javax.security.auth.Subject

class ChapterRepository{

    companion object {
        const val collectionRoot = "chapters"
        const val lecturePath = "lectures"
    }
    private val TAG = "ChapterRepository"
    private val db = FirebaseFirestore.getInstance()

    data class InsertData(
        val db: FirebaseFirestore,
        val lecture: Lecture
    )

    class InsertAsyncTask: AsyncTask<InsertData, Unit, Unit>(){
        override fun doInBackground(vararg p0: InsertData?){
            p0[0]!!.db.collection(collectionRoot).document(p0[0]!!.lecture.ofChapter.toString())
                .set(p0[0]!!.lecture)
                .addOnSuccessListener {
                    Log.d("ChapterRepository", "Successfully added lecture")
                }
                .addOnFailureListener {
                    Log.d("ChapterRepository", "Failure adding Lecture: $it")
                }
        }
    }
    fun insert(lecture: Lecture) {
        InsertAsyncTask().execute(InsertData(db, lecture))
    }

    fun delete(chapter: Chapter) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun update(chapter: Chapter) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getAllChapters(standard: String, subject: Int): Query{
        Log.d(TAG, "$standard: $subject")
        return db.collection(collectionRoot)
            .whereEqualTo("standard", standard)
            .whereEqualTo("subject", subject)
    }
}

class LectureRepository(resources: Resources): LectureDAO {
    private val lectureArray = arrayListOf(
        "Lecture 1: Electric Current and drift velocity",
        "Lecture 2: Resistivity - defination and formula",
        "Lecture 3: Colour Resistivity of carbon Resistance"
    )
    private val imageArray = arrayListOf(
        BitmapFactory.decodeResource(resources, R.drawable.physics),
        BitmapFactory.decodeResource(resources, R.drawable.phy),
        BitmapFactory.decodeResource(resources, R.drawable.electostatics)
    )

    private var lectureLiveData: MutableLiveData<List<LectureData>> = MutableLiveData()

    init {
        val listStr = ArrayList<String>()
        listStr.addAll(lectureArray)
        val lectures = ArrayList<LectureData>()
        for (i in 0..(listStr.size - 1)) {
            lectures.add(LectureData(i.toString(), listStr[i], imageArray[i]))
        }
        lectureLiveData.value = lectures
    }
    override fun insert(lecture: LectureData) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(lecture: LectureData) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(lecture: LectureData) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllLectures(): MutableLiveData<List<LectureData>> {

        return lectureLiveData
    }

}