package com.example.e_education

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.e_education.utils.Activity
import com.example.e_education.viewmodel.LectureData
import com.example.e_education.utils.LectureRecyclerViewAdapter
import com.example.e_education.viewmodel.LectureViewModel
import kotlin.math.max

class LectureActivity : AppCompatActivity() {

    lateinit var model: LectureViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture)
        title = intent.getStringExtra("chapter")

        // Initiating the Data model
        model = ViewModelProviders.of(this).get(LectureViewModel::class.java)
        model.init(resources)
        // Initiate recyclerView
        val recyclerView: RecyclerView = findViewById(R.id.lectureRecyclerView)
        val adapter = LectureRecyclerViewAdapter()
        recyclerView.adapter = adapter
        model.getLectureList().observe(this, Observer {
            adapter.setData(ArrayList(it))
            Log.d("tag", it.toString())
        })
        recyclerView.layoutManager = LinearLayoutManager(
                applicationContext,
                RecyclerView.VERTICAL,
                false
            )
        recyclerView.addItemDecoration(
                DividerItemDecoration(
                    this,
                    DividerItemDecoration.VERTICAL
                )
            )
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_action_bar, menu)
        val searchView = menu?.findItem(R.id.search_bar)?.actionView as SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean = true
            override fun onQueryTextChange(p0: String?): Boolean {
                model.search(p0)
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.search_bar)
            Toast.makeText(applicationContext, "Not implemented", Toast.LENGTH_LONG).show()
        return super.onOptionsItemSelected(item)
    }

    fun onAddButtonClicked(view: View){
        val intent = Intent(this, PublishVideoActivity::class.java)
        intent.putExtra("activity", Activity.LectureActivity)
        startActivity(intent)
    }
}
