package com.example.e_education

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.Toolbar
import android.util.Log
import com.example.e_education.utils.ChaptersRecyclerViewAdapter

class SubjectsActivity : AppCompatActivity() {
    private val TAG = "E-Education"
    private val chapterNameList = arrayListOf("What is physics?", "Vectors", "NLM", "Kinematics","What is physics?", "Vectors", "NLM", "Kinematics","What is physics?", "Vectors", "NLM", "Kinematics")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subjects)
        setSupportActionBar(findViewById(R.id.subjectsToolbar))
        supportActionBar!!.title = intent.getStringExtra("subject")
        val chapterList: RecyclerView = findViewById(R.id.chaptersList)
        chapterList.apply {
            adapter = ChaptersRecyclerViewAdapter(chapterNameList)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
    }
}
