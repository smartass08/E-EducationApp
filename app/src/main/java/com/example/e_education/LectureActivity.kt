package com.example.e_education

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.Toast
import com.example.e_education.utils.LectureRecyclerViewAdapter
import java.lang.Exception
import java.net.URL

class LectureActivity : AppCompatActivity() {

    val urls  = arrayListOf("https://homepages.cae.wisc.edu/~ece533/images/airplane.png",
        "https://homepages.cae.wisc.edu/~ece533/images/arctichare.png",
        "https://homepages.cae.wisc.edu/~ece533/images/baboon.png")

    val lectureArray = arrayListOf("Lecture 1: Electric Current and drift velocity",
        "Lecture 2: Resistivity - defination and formula",
        "Lecture 3: Colour Resistivity of carbon Resistance")
    val imageArray = ArrayList<Bitmap>()
    class imageDownloader(val imageArray: ArrayList<Bitmap>, val context: Context): AsyncTask<String, Void, Bitmap?>(){

        override fun onPreExecute() {

        }
        override fun doInBackground(vararg p0: String?): Bitmap? {
            val urlImage = p0[0]
            return try{
                val url = URL(urlImage).openStream()
                BitmapFactory.decodeStream(url)
            }
            catch (e: Exception){
                e.printStackTrace()
                return null
            }
        }

        override fun onPostExecute(result: Bitmap?) {
            if(result != null){
                imageArray.add(result)
            }
            else{
                imageArray.add(BitmapFactory.decodeResource(context.resources, R.drawable.physics))
            }
        }

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture)
        supportActionBar!!.title = intent.getStringExtra("chapter")

        val imageArray2 = arrayListOf(BitmapFactory.decodeResource(resources, R.drawable.physics),
            BitmapFactory.decodeResource(resources, R.drawable.phy),
                BitmapFactory.decodeResource(resources, R.drawable.electostatics))
        val recyclerView: RecyclerView = findViewById(R.id.lectureRecyclerView)
        recyclerView.apply {
            adapter = LectureRecyclerViewAdapter(lectureArray, imageArray2)
            layoutManager = LinearLayoutManager(
                applicationContext,
                LinearLayoutManager.VERTICAL,
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
        if(item!!.itemId == R.id.search_bar)
            Toast.makeText(applicationContext, "Not implemented", Toast.LENGTH_LONG).show()
        return super.onOptionsItemSelected(item)
    }
}
