package com.example.e_education.utils

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.e_education.R
import java.lang.StringBuilder


class ChaptersRecyclerViewAdapter(private val chapterName: ArrayList<String>) :
    RecyclerView.Adapter<ChaptersRecyclerViewAdapter.ChaptersViewHolder>(){

    class ChaptersViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val chapterName: TextView = itemView.findViewById(R.id.chapterNameTextView)
        val chapterNumber: TextView = itemView.findViewById(R.id.chaptersNumberTextView)
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ChaptersViewHolder {
        val itemView = LayoutInflater.from(p0.context).inflate(R.layout.chapter_name_list_layout, p0, false)
        return ChaptersViewHolder(itemView)
    }

    override fun getItemCount(): Int = chapterName.size

    override fun onBindViewHolder(p0: ChaptersViewHolder, i: Int) {
        p0.chapterName.text = chapterName[i]
        p0.chapterNumber.text = StringBuilder("${i + 1}").toString()
    }


}