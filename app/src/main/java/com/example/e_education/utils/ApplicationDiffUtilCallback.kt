package com.example.e_education.utils

import android.os.Bundle
import androidx.recyclerview.widget.DiffUtil
import com.example.e_education.viewmodel.Chapter
import com.example.e_education.viewmodel.LectureData

class ChaptersDiffUtilCallback(private val oldDataSet: List<Chapter>,private  val newDataSet: List<Chapter>):
    DiffUtil.Callback(){

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldDataSet[oldItemPosition].id == newDataSet[newItemPosition].id
        }

        override fun getOldListSize(): Int = oldDataSet.size

        override fun getNewListSize(): Int = newDataSet.size

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldDataSet[oldItemPosition].chapterName == newDataSet[newItemPosition].chapterName
        }

}

class LecturesDiffUtilCallback(private val oldDataSet: List<LectureData>, private  val newDataSet: List<LectureData>):
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