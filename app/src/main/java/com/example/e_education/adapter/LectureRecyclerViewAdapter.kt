package com.example.e_education.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.e_education.R
import com.example.e_education.models.Lecture
import com.example.e_education.utils.LecturesDiffUtilCallback
import com.example.e_education.utils.TAG
import com.example.e_education.utils.load

class LectureRecyclerViewAdapter:
        RecyclerView.Adapter<LectureRecyclerViewAdapter.ViewHolder>(){

    private var data: List<Lecture> = ArrayList()
    private var oldData: List<Lecture> = ArrayList()
    fun setData(newData: List<Lecture>){
        oldData = data
        data = newData.sortedBy { it.lectureNum }
        val diffUtil = DiffUtil.calculateDiff(LecturesDiffUtilCallback(oldData, data), true)
        diffUtil.dispatchUpdatesTo(this)
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ViewHolder {
        val itemView = LayoutInflater.from(p0.context).inflate(R.layout.lecture_list_layout, p0, false)
        return ViewHolder(itemView)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(p0: ViewHolder, p1: Int) {
        p0.lectureNum.text = "${data[p1].lectureNum}"

        p0.lectureNameView.text = data[p1].lectureName
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        val lectureNum: TextView = view.findViewById(R.id.lectureNumbTextView)
        val lectureNameView: TextView = view.findViewById(R.id.lectureName)
    }
}