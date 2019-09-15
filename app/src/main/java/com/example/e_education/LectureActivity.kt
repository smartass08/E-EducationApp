package com.example.e_education

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_education.adapter.LectureRecyclerViewAdapter
import com.example.e_education.models.*
import com.example.e_education.models.factory.ChaptersViewModelFactory
import com.example.e_education.models.factory.VideoPlayerViewModelFactory
import com.example.e_education.utils.ActivityIndex
import com.example.e_education.utils.MediaSourceFactory
import com.example.e_education.utils.getExtra
import com.example.e_education.utils.putExtra
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_lecture.*

class LectureActivity : AppCompatActivity() {

    lateinit var model: ChaptersViewModel
    private val auth = FirebaseAuth.getInstance()
    private val currUser = auth.currentUser
    private val TAG = "LectureActivity"
    private var data: IntentData? = null
    private var videoModel: VideoPlayerViewModel? = null
    override fun onStart() {
        super.onStart()
        if (currUser != null && currUser.uid == BuildConfig.AdminUID){
            Log.d(TAG, "Admin")
            publishButton.show()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture)
        data = intent.getExtra(IntentData.name, IntentData::class.java)

        val videoProvider = VideoPlayerViewModelFactory(this)
        videoModel = ViewModelProviders.of(this, videoProvider).get(VideoPlayerViewModel::class.java)
        player_view.player = videoModel?.getPlayer()
        player_view.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL

        // Initiating the Data model
        val provider = ChaptersViewModelFactory(
            data!!.user.standard,
            data!!.subject,
            intent.getIntExtra("chapterNum", -1)
        )

        model = ViewModelProviders.of(this, provider).get(ChaptersViewModel::class.java)
            if (model.authUser != null && model.authUser?.uid == BuildConfig.AdminUID) {
                Log.d(TAG, "Admin")
                publishButton.show()
            }
                // Initiate recyclerView
                val recyclerView: RecyclerView = findViewById(R.id.lectureRecyclerView)
                val adapter = LectureRecyclerViewAdapter()
                recyclerView.adapter = adapter

                model.getChapterList()?.observe(this, Observer<List<Lecture>> {
                    adapter.setData(ArrayList(it))
                    Log.d("tag", data.toString())
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

    override fun onResume() {
        super.onResume()
        videoModel?.resume()
        hideSystemUi()
    }

    override fun onPause() {
        super.onPause()
        videoModel?.pause()
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if(item?.itemId == R.id.search_bar)
            Toast.makeText(applicationContext, "Not implemented", Toast.LENGTH_LONG).show()
        return super.onOptionsItemSelected(item)
    }

    fun onAddButtonClicked(view: View){
        val intent = Intent(this, PublishVideoActivity::class.java)
        intent.putExtra(IntentData.name, IntentData(data!!, ActivityIndex.LectureActivity))
        startActivity(intent)
    }


    private fun hideSystemUi() {
        player_view.systemUiVisibility = (View.SYSTEM_UI_FLAG_LOW_PROFILE
                or View.SYSTEM_UI_FLAG_FULLSCREEN
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION)
    }

}