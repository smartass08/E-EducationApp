package com.example.e_education.utils

import androidx.recyclerview.widget.DiffUtil
import com.example.e_education.models.Lecture

class ChaptersDiffUtilCallback(private val oldDataSet: List<Lecture>,private  val newDataSet: List<Lecture>):
    DiffUtil.Callback(){

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldDataSet[oldItemPosition].ofChapter == newDataSet[newItemPosition].ofChapter
        }

        override fun getOldListSize(): Int = oldDataSet.size

        override fun getNewListSize(): Int = newDataSet.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldDataSet[oldItemPosition].chapterName == newDataSet[newItemPosition].chapterName
        }

}

class LecturesDiffUtilCallback(private val oldDataSet: List<Lecture>, private  val newDataSet: List<Lecture>):
    DiffUtil.Callback(){

    companion object {
        const val lectureKey = "lectureName"
    }
    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldDataSet[oldItemPosition].id == newDataSet[newItemPosition].id
    }

    override fun getOldListSize(): Int = oldDataSet.size

    override fun getNewListSize(): Int = newDataSet.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldDataSet[oldItemPosition].lectureName == newDataSet[newItemPosition].lectureName
    }
}