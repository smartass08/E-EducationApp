package com.example.e_education

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.Menu
import com.example.e_education.utils.ChaptersRecyclerViewAdapter

class SubjectsActivity : AppCompatActivity() {
    private val TAG = "E-Education"
    private val chapterNameList = arrayListOf("What is physics?", "Vectors", "NLM", "Kinematics","What is physics?", "Vectors", "NLM", "Kinematics","What is physics?", "Vectors", "NLM", "Kinematics")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subjects)
        supportActionBar!!.title = intent.getStringExtra("subject")
        val chapterList: RecyclerView = findViewById(R.id.chaptersList)
        val adapter = ChaptersRecyclerViewAdapter(chapterNameList)
        adapter.setOnClickListener {
            val intent = Intent(applicationContext, LectureActivity::class.java)
            intent.putExtra("chapter", adapter.getChapterNameTextView(it).text)
            startActivity(intent)
        }
        chapterList.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(
                context, RecyclerView.VERTICAL,
                false
            )
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_action_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }
}
