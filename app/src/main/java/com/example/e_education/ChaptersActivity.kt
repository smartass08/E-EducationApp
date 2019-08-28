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
import com.example.e_education.models.IntentData
import com.example.e_education.models.Lecture
import com.example.e_education.models.factory.ChaptersViewModelFactory
import com.example.e_education.utils.ActivityIndex
import com.example.e_education.utils.SubjectNumber
import com.example.e_education.utils.getExtra
import com.example.e_education.utils.putExtra
import kotlinx.android.synthetic.main.activity_chapters.*

class SubjectsActivity : AppCompatActivity() {

    private val TAG = "E-Education"
    private lateinit var model: ChaptersViewModel
    private var adapter: ChaptersRecyclerViewAdapter? = null
    private lateinit var chapterList: RecyclerView
    private var data: IntentData? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chapters)
        data = intent.getExtra(IntentData.name, IntentData::class.java)
        title = SubjectNumber.toString(data!!.subject)
        // Initialising out view model
        val provider = ChaptersViewModelFactory(data!!.user.standard, data!!.subject)
        model = ViewModelProviders.of(this, provider).get(ChaptersViewModel::class.java)
        if (model.authUser != null && model.authUser?.uid == BuildConfig.AdminUID) {
            Log.d(TAG, "Admin")
            publishButton.show()
        }

        progressBar.visibility = View.GONE
        // Initialising the RecyclerView
        chapterList = findViewById(R.id.chaptersList)
        adapter = ChaptersRecyclerViewAdapter()
        chapterList.adapter = adapter!!
        model.getChapterList()?.observe(this, Observer<List<Lecture>> {
            if (it.isNullOrEmpty()) {
                adapter?.setData(emptyList())
            } else {
                val arr = it.distinctBy { it.ofChapter }
                adapter?.setData(arr.sortedBy { it.ofChapter })
                Log.d(TAG, it.toString())
            }
        })
        adapter!!.setOnClickListener {
            val intent = Intent(applicationContext, LectureActivity::class.java)
            val d = IntentData(data!!, ActivityIndex.ChaptersActivity)
            intent.putExtra(IntentData.name, d)
            intent.putExtra("chapter", adapter!!.getChapterNameTextView(it).text)
            val clickedChapterNum = adapter!!.getChapterNumTextView(it).text
            intent.putExtra("chapterNum", clickedChapterNum)
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
                model.search(p0)
                return true
            }

        })
        return super.onCreateOptionsMenu(menu)
    }

    fun onAddButtonClicked(view: View){
        val i = Intent(this, PublishVideoActivity::class.java)
        val sendData = IntentData(data!!, ActivityIndex.ChaptersActivity)
        i.putExtra(IntentData.name, sendData)
        startActivity(i)
    }
}
