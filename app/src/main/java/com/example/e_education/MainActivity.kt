package com.example.e_education

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.view.ViewPager
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.e_education.utils.ContinueWatchingListAdapter
import me.relex.circleindicator.CircleIndicator
import com.example.e_education.utils.SliderPageAdapter
import android.view.View

class MainActivity : AppCompatActivity() {
    private val sliderDelay: Long = 3000
    private val imageArray = arrayListOf(R.drawable.electostatics, R.drawable.phy, R.drawable.physics)
    private val caption = arrayListOf("Electrostats L-04", "Electrochemistry L-08", "What is Physics?")
    lateinit var sliderAdapter: SliderPageAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(findViewById(R.id.my_toolbar))

        // Code to activate the automatic Image Slider
        val imageSlider = findViewById<ViewPager>(R.id.uploadSlider)
        sliderAdapter = SliderPageAdapter(applicationContext, imageArray)
        sliderAdapter.setAutoSlideDuration(imageSlider, sliderDelay)
        imageSlider.adapter = sliderAdapter
        val circleIndicator = findViewById<CircleIndicator>(R.id.circleIndicator)
        circleIndicator.setViewPager(imageSlider)


        // Code for Continue Watching Recycler View
        val recyclerView = findViewById<RecyclerView>(R.id.continueWatchingList)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(applicationContext, LinearLayoutManager.HORIZONTAL, false)
            adapter = ContinueWatchingListAdapter(this@MainActivity, imageArray, caption)
            addItemDecoration(DividerItemDecoration(applicationContext, DividerItemDecoration.HORIZONTAL))

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_action_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.search_bar)
            Toast.makeText(applicationContext, "Search Not yet implemented", Toast.LENGTH_LONG).show()
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        sliderAdapter.onActivityDestroyed()
    }
    fun onSubjectButtonClicked(view: View){
        Toast.makeText(applicationContext, "Not implemented", Toast.LENGTH_LONG).show()
    }
}
