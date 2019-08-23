package com.example.e_education.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.e_education.R
import com.example.e_education.models.Lecture
import com.example.e_education.utils.ChaptersDiffUtilCallback


class ChaptersRecyclerViewAdapter :
        RecyclerView.Adapter<ChaptersRecyclerViewAdapter.ChaptersViewHolder>(){

    private var chapters: List<Lecture> = ArrayList()
    private var oldChapters: List<Lecture> = ArrayList()

    fun setData(data: List<Lecture>){
        oldChapters = chapters
        chapters = data
        val diffUtil = DiffUtil.calculateDiff(
            ChaptersDiffUtilCallback(
                oldChapters,
                chapters
            ), true
        )
        diffUtil.dispatchUpdatesTo(this)
    }
    private var onClick: (view: View) -> Unit = {}
    fun setOnClickListener(callback: (view: View) -> Unit){
        onClick = callback
    }
    fun getChapterNameTextView(view: View): TextView{
        return (view as LinearLayout).getChildAt(1) as TextView
    }
    fun getChapterNumTextView(view: View): TextView{
        return (view as LinearLayout).getChildAt(0) as TextView
    }
    class ChaptersViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val chapterName: TextView = itemView.findViewById(R.id.chapterNameTextView)
        val chapterNumber: TextView = itemView.findViewById(R.id.chaptersNumberTextView)
        val chapterNameRoot: LinearLayout = itemView.findViewById(R.id.layoutRoot)
    }
    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): ChaptersViewHolder {
        val itemView = LayoutInflater.from(p0.context).inflate(R.layout.chapter_name_list_layout, p0, false)
        return ChaptersViewHolder(itemView)
    }

    override fun getItemCount(): Int = chapters.size

    override fun onBindViewHolder(p0: ChaptersViewHolder, i: Int) {
        p0.chapterName.text = chapters[i].chapterName
        p0.chapterNumber.text = "${chapters[i].ofChapter}"
        p0.chapterNameRoot.setOnClickListener(onClick)
    }
}

