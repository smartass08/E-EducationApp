package com.example.e_education

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager.widget.ViewPager
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import me.relex.circleindicator.CircleIndicator
import android.view.View
import android.widget.TextView
import com.example.e_education.utils.*
import com.example.e_education.models.ContinueWatchingData
import com.example.e_education.models.SliderData
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlin.math.max

class MainActivity : AppCompatActivity() {
    private val TAG = "E-Education"
    private val sliderDelay: Long = 3000

    private val imageArray = arrayListOf(R.drawable.electostatics, R.drawable.phy, R.drawable.physics)
    private val captionArray = arrayListOf("Electrostats L-04", "Electrochemistry L-08", "What is Physics?")
    private lateinit var sliderAdapter: SliderPageAdapter
    private val continueWatchingDataArray = arrayListOf<ContinueWatchingData>()
    private val sliderDataArray = arrayListOf<SliderData>()
    private val db = FirebaseFirestore.getInstance()
    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        for (i in 0..(max(imageArray.size, captionArray.size) - 1)){
            sliderDataArray.add(SliderData(i, imageArray[i]))
        }
        val imageSlider = findViewById<ViewPager>(R.id.uploadSlider)
        // Code to activate the automatic Image Slider
        sliderAdapter = SliderPageAdapter(applicationContext, sliderDataArray)
        sliderAdapter.setAutoSlideDuration(imageSlider, sliderDelay)
        imageSlider.adapter = sliderAdapter
        val circleIndicator = findViewById<CircleIndicator>(R.id.circleIndicator)
        circleIndicator.setViewPager(imageSlider)


        // Code for Continue Watching Recycler View
        for (i in 0..(max(imageArray.size, captionArray.size) - 1)){
            continueWatchingDataArray.add(
                ContinueWatchingData(i,
                    imageArray[i],
                    captionArray[i]
                )
            )
        }
        val recyclerView = findViewById<RecyclerView>(R.id.continueWatchingList)
        recyclerView.apply {
            layoutManager = LinearLayoutManager(
                applicationContext,
                RecyclerView.HORIZONTAL,
                false
            )
            adapter = ContinueWatchingListAdapter(this@MainActivity, continueWatchingDataArray)
            addItemDecoration(
                DividerItemDecoration(
                    applicationContext,
                    DividerItemDecoration.HORIZONTAL
                )
            )

        }

    }
    private fun logout(){
        auth.signOut()
        finish()
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_action_bar, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item!!.itemId == R.id.search_bar)
            Toast.makeText(applicationContext, "Search Not yet implemented", Toast.LENGTH_LONG).show()
        else if (item.itemId == R.id.logout)
            logout()
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        sliderAdapter.onActivityDestroyed()
    }

    @SuppressWarnings
    fun onSubjectButtonClicked(view: View){
        val text = ((view as CardView).getChildAt(0) as TextView).text.toString()
        val intent = Intent(this, SubjectsActivity::class.java).apply {
            putExtra("subject", text)
        }
        startActivity(intent)
    }
}
