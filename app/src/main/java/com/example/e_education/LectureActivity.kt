package com.example.e_education

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_education.adapter.LectureRecyclerViewAdapter
import com.example.e_education.models.*
import com.example.e_education.models.factory.ChaptersViewModelFactory
import com.example.e_education.models.factory.VideoPlayerViewModelFactory
import com.example.e_education.utils.*
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_lecture.*

class LectureActivity : AppCompatActivity(),
    View.OnTouchListener,
    GestureDetector.OnGestureListener,
    GestureDetector.OnDoubleTapListener {

    private lateinit var model: ChaptersViewModel
    private val auth = FirebaseAuth.getInstance()
    private val currUser = auth.currentUser
    private val TAG = "LectureActivity"
    private var data: IntentData? = null
    private var videoModel: VideoPlayerViewModel? = null
    private var mGestureDetector: GestureDetector? = null

    override fun onStart() {
        super.onStart()
        if (currUser != null && currUser.uid == BuildConfig.AdminUID) {
            Log.d(TAG, "Admin")
            publishButton.show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_lecture)
        data = intent.getExtra(IntentData.name, IntentData::class.java)

        mGestureDetector = android.view.GestureDetector(this, this)

        // Initiating the Data model
        val provider = ChaptersViewModelFactory(
            data!!.user.standard,
            data!!.subject,
            intent.getIntExtra("chapterNum", -1)
        )

        model = ViewModelProviders.of(this, provider).get(ChaptersViewModel::class.java)

        player_view.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
        player_view.setOnTouchListener(this)

        val adapter = LectureRecyclerViewAdapter(this)
        if (model.authUser != null && model.authUser?.uid == BuildConfig.AdminUID) {
            publishButton.show()
        }
        // Initiate recyclerView
        val recyclerView: RecyclerView = findViewById(R.id.lectureRecyclerView)
        recyclerView.adapter = adapter
        adapter.setOnItemClickListener {
            videoModel?.changeMedia(Uri.parse(adapter.getData(it).id))
        }
        model.getChapterList()?.observe(this, Observer<List<Lecture>> {
            val videoProvider = VideoPlayerViewModelFactory(this, Uri.parse(it[0].id))
            videoModel = ViewModelProviders.of(this, videoProvider).get(VideoPlayerViewModel::class.java)
            player_view.player = videoModel?.getPlayer()
            adapter.setData(ArrayList(it))
            data?.log(TAG)
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
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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
        if (item?.itemId == R.id.search_bar)
            Toast.makeText(applicationContext, "Not implemented", Toast.LENGTH_LONG).show()
        return super.onOptionsItemSelected(item)
    }

    /**************** FOR VIDEO PLAYER **************/
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        mGestureDetector?.onTouchEvent(event)
        return true
    }

    /****************** IMPLEMENTING DOUBLE TAP TO FORWARD - REWIND**********************/
    override fun onDoubleTap(e: MotionEvent?): Boolean {
        if (e?.x!!.compareTo(player_view.width / 2) < 0) {
            // Clicked on the LEFT side. Rewind 10s
            videoModel?.rewind()
            return true
        } else if (e.x.compareTo(player_view.width / 2) > 0) {
            // Clicked on the RIGHT side. Forward 10s
            videoModel?.forward()
            return true
        }
        return false
    }

    override fun onDoubleTapEvent(e: MotionEvent?): Boolean = false
    override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
        player_view.performClick()
        return false
    }

    override fun onShowPress(e: MotionEvent?) {}
    override fun onSingleTapUp(e: MotionEvent?): Boolean = false
    override fun onDown(e: MotionEvent?): Boolean = false
    override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean = false
    override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean = false
    override fun onLongPress(e: MotionEvent?) {}

    fun onAddButtonClicked(view: View) {
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