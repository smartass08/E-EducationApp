package com.example.e_education

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.e_education.utils.LectureData
import com.example.e_education.utils.LectureRecyclerViewAdapter
import kotlin.math.max

class LectureActivity : AppCompatActivity() {

    private val lectureArray = arrayListOf("Lecture 1: Electric Current and drift velocity",
        "Lecture 2: Resistivity - defination and formula",
        "Lecture 3: Colour Resistivity of carbon Resistance")

    private var imageArray = arrayListOf<Bitmap>()

    private var lectureDataArray = ArrayList<LectureData>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture)
        supportActionBar!!.title = intent.getStringExtra("chapter")


        imageArray = arrayListOf(BitmapFactory.decodeResource(resources, R.drawable.physics),
        BitmapFactory.decodeResource(resources, R.drawable.phy),
        BitmapFactory.decodeResource(resources, R.drawable.electostatics))

        for (i in 0..(max(imageArray.size, lectureArray.size) - 1)){
            lectureDataArray.add(LectureData(lectureArray[i], imageArray[i]))
        }

        val recyclerView: RecyclerView = findViewById(R.id.lectureRecyclerView)
        recyclerView.apply {
            adapter = LectureRecyclerViewAdapter(lectureDataArray)
            layoutManager = LinearLayoutManager(
                applicationContext,
                RecyclerView.VERTICAL,
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

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.search_bar)
            Toast.makeText(applicationContext, "Not implemented", Toast.LENGTH_LONG).show()
        return super.onOptionsItemSelected(item)
    }
}
