package com.example.e_education

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_education.adapter.LectureRecyclerViewAdapter
import com.example.e_education.models.ChaptersViewModel
import com.example.e_education.models.IntentData
import com.example.e_education.models.Lecture
import com.example.e_education.models.User
import com.example.e_education.utils.ActivityIndex
import com.example.e_education.utils.getExtra
import com.example.e_education.utils.putExtra
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_lecture.*

class LectureActivity : AppCompatActivity() {

    lateinit var model: ChaptersViewModel
    private val auth = FirebaseAuth.getInstance()
    private val currUser = auth.currentUser
    private val TAG = "LectureActivity"
    private var data: IntentData? = null

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
        val mTitle = intent.getStringExtra("chapter")
        title = mTitle

        // Initiating the Data model
        model = ViewModelProviders.of(this).get(ChaptersViewModel::class.java)
        model.currUser.observe(this, Observer<User> {
            if (model.authUser != null && model.authUser?.uid == BuildConfig.AdminUID) {
                Log.d(TAG, "Admin")
                publishButton.show()
            }
            if (it != null) {
                model.init(
                    data!!.user.standard, data!!.subject,
                    intent.getStringExtra("chapterNum").toInt())

                // Initiate recyclerView
                val recyclerView: RecyclerView = findViewById(R.id.lectureRecyclerView)
                val adapter = LectureRecyclerViewAdapter()
                recyclerView.adapter = adapter

                model.getChapterList()?.observe(this, Observer<List<Lecture>> {
                    val data = it.sortedBy { it.lectureName }
                    adapter.setData(ArrayList(data))
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
        })
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
        intent.putExtra(IntentData.name, IntentData(data!!, ActivityIndex.LectureActivity))
        startActivity(intent)
    }
}
