package com.example.e_education.utils

import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import com.example.e_education.R
import com.example.e_education.viewmodel.LectureData

class LectureRecyclerViewAdapter:
        RecyclerView.Adapter<LectureRecyclerViewAdapter.ViewHolder>(){

    private var data: List<LectureData> = ArrayList()
    private var oldData: List<LectureData> = ArrayList()
    fun setData(newData: List<LectureData>){
        oldData = data
        data = newData
        val diffUtil = DiffUtil.calculateDiff(LecturesDiffUtilCallback(oldData, data), true)
        diffUtil.dispatchUpdatesTo(this)
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val itemView = LayoutInflater.from(p0.context).inflate(R.layout.lecture_list_layout, p0, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.lectureIcon.setImageBitmap(data[p1].lectureImage)
        p0.lectureNameView.text = data[p1].lectureName
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val lectureIcon: ImageView = view.findViewById(R.id.lectureImage)
        val lectureNameView: TextView = view.findViewById(R.id.lectureName)
    }
}