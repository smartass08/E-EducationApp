package com.example.e_education

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.e_education.utils.ActivityIndex
import com.example.e_education.utils.ChaptersRecyclerViewAdapter
import com.example.e_education.models.Chapter
import com.example.e_education.models.ChaptersViewModel
import com.example.e_education.models.User
import com.example.e_education.utils.SubjectNumber
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_chapters.*

class SubjectsActivity : AppCompatActivity() {
    private val TAG = "E-Education"
    private var model: ChaptersViewModel? = null
    private var adapter: ChaptersRecyclerViewAdapter? = null
    private lateinit var chapterList: RecyclerView
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val currUser = auth.currentUser
    private var mUser: User? = null

    override fun onStart() {
        super.onStart()
        if (currUser != null && currUser.uid == BuildConfig.AdminUID){
            Log.d(TAG, "Admin")
            publishButton.show()
            val docRef = db.collection(User.USER_FIELD_NAME).document(currUser.uid)
            docRef.get().addOnSuccessListener {
                mUser = it.toObject(User::class.java)
            }
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapters)
        val mTitle = intent.getStringExtra("subject")
        title = mTitle
        // Initialising out view model
        model = ViewModelProviders.of(this).get(ChaptersViewModel::class.java)
        model!!.init("XI", SubjectNumber.toKey(mTitle))
        // Initialising the RecyclerView
        chapterList = findViewById(R.id.chaptersList)
        adapter = ChaptersRecyclerViewAdapter()
        chapterList.adapter = adapter
        model!!.getChapterList()?.observe(this, Observer<List<Chapter>>{
              if (it.isNullOrEmpty()){
                  adapter?.setData(emptyList())
              } else {
                  adapter?.setData(it)
                  Log.d(TAG, it.toString())
              }
        })
        adapter!!.setOnClickListener {
            val intent = Intent(applicationContext, LectureActivity::class.java)
            intent.putExtra("chapter", adapter!!.getChapterNameTextView(it).text)
            startActivity(intent)
        }
        chapterList.layoutManager = LinearLayoutManager(
                applicationContext, RecyclerView.VERTICAL,
                false
            )
        chapterList.addItemDecoration(
                DividerItemDecoration(
                    applicationContext,
                    DividerItemDecoration.VERTICAL
                )
            )
        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home_action_bar, menu)
        val searchView = menu?.findItem(R.id.search_bar)?.actionView as SearchView
        val manager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(manager.getSearchableInfo(componentName))

        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(p0: String?): Boolean = true

            override fun onQueryTextChange(p0: String?): Boolean {
                model!!.search(p0)
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    fun onAddButtonClicked(view: View){
        val intent = Intent(this, PublishVideoActivity::class.java)
        intent.putExtra("activity", ActivityIndex.ChaptersActivity)
        startActivity(intent)
    }
}
