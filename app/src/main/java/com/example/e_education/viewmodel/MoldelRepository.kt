package com.example.e_education.viewmodel

import android.content.res.Resources
import android.graphics.BitmapFactory
import androidx.lifecycle.MutableLiveData
import com.example.e_education.R


class ChapterRepository: ChapterDAO{
    private val dbChapterNames = arrayListOf(
        "What is physics?", "Vectors", "NLM", "Kinematics",
        "What is physics?", "Vectors", "NLM", "Kinematics",
        "What is physics?", "Vectors", "NLM", "Kinematics")

    private var filteredChapterName: MutableLiveData<List<Chapter>>? = null
    init {
            val listStr = ArrayList<String>()
            listStr.addAll(dbChapterNames)
            val chapters = ArrayList<Chapter>()
            for (i in 0..(listStr.size - 1)) {
                chapters.add(Chapter(i, listStr[i]))
            }
            filteredChapterName = MutableLiveData()
            filteredChapterName?.value = chapters
        }

    override fun insert(chapter: Chapter) {
        dbChapterNames.add(chapter.chapterName!!)
    }

    override fun delete(chapter: Chapter) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(chapter: Chapter) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getAllChapters(): MutableLiveData<List<Chapter>>? {
        return filteredChapterName
    }

}

class LectureRepository(resources: Resources): LectureDAO{
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
            lectures.add(LectureData(i, listStr[i], imageArray[i]))
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