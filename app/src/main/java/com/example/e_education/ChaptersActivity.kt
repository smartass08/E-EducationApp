package com.example.e_education

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.e_education.adapter.ChaptersRecyclerViewAdapter
import com.example.e_education.models.ChaptersViewModel
import com.example.e_education.models.Lecture
import com.example.e_education.models.User
import com.example.e_education.utils.ActivityIndex
import com.example.e_education.utils.SubjectNumber
import kotlinx.android.synthetic.main.activity_chapters.*

class SubjectsActivity : AppCompatActivity() {

    private val TAG = "E-Education"
    private lateinit var model: ChaptersViewModel
    private var adapter: ChaptersRecyclerViewAdapter? = null
    private lateinit var chapterList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapters)
        val mTitle = SubjectNumber.toString(intent.getIntExtra("subject", -1))
        title = mTitle

        // Initialising out view model
        model = ViewModelProviders.of(this).get(ChaptersViewModel::class.java)
        model.currUser.observe(this, Observer<User> {
            if (model.authUser != null && model.authUser?.uid == BuildConfig.AdminUID) {
                Log.d(TAG, "Admin")
                publishButton.show()
            }
            if (it != null) {
                model.init(it.standard, SubjectNumber.toKey(mTitle))
                progressBar.visibility = View.GONE
                // Initialising the RecyclerView
                chapterList = findViewById(R.id.chaptersList)
                adapter = ChaptersRecyclerViewAdapter()
                chapterList.adapter = adapter!!
                model.getChapterList()?.observe(this, Observer<List<Lecture>> {
                    if (it.isNullOrEmpty()) {
                        adapter?.setData(emptyList())
                    } else {
                        val data = it.distinctBy { it.ofChapter }
                        adapter?.setData(data.sortedBy { it.ofChapter })
                        Log.d(TAG, it.toString())
                    }
                })
                adapter!!.setOnClickListener {
                    val intent = Intent(applicationContext, LectureActivity::class.java)
                    intent.putExtra("chapter", adapter!!.getChapterNameTextView(it).text)

                    val clickedChapterNum = adapter!!.getChapterNumTextView(it).text
                    intent.putExtra("chapterNum", clickedChapterNum)
                    intent.putExtra("subject", SubjectNumber.toKey(mTitle))
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

        }
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
                model.search(p0)
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    fun onAddButtonClicked(view: View){
        val intent = Intent(this, PublishVideoActivity::class.java)
        intent.putExtra("activity", ActivityIndex.ChaptersActivity)
        intent.putExtra("subject", SubjectNumber.toKey(title.toString()))
        intent.putExtra("standard", model.currUser.value!!.standard)
        startActivity(intent)
    }
}
