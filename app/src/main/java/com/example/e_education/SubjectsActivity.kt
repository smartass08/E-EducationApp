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
import android.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DiffUtil
import com.example.e_education.utils.ChaptersDiffUtilCallback
import com.example.e_education.utils.ChaptersRecyclerViewAdapter
import com.example.e_education.viewmodel.Chapter
import com.example.e_education.viewmodel.SubjectsActivityViewModel
import com.jakewharton.rxbinding2.widget.RxSearchView
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import java.util.concurrent.TimeUnit

class SubjectsActivity : AppCompatActivity() {
    private val TAG = "E-Education"
    private var model: SubjectsActivityViewModel? = null
    private var adapter: ChaptersRecyclerViewAdapter? = null
    private val disposable = CompositeDisposable()
    private lateinit var chapterList: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subjects)
        supportActionBar!!.title = intent.getStringExtra("subject")

        // Initialising out view model
        model = ViewModelProviders.of(this).get(SubjectsActivityViewModel::class.java)

        // Initialising the RecyclerView
        chapterList = findViewById(R.id.chaptersList)
        adapter = ChaptersRecyclerViewAdapter()
        chapterList.adapter = adapter
        model!!.getChapterList()?.observe(this, Observer<List<Chapter>>{
              adapter?.setData(it!!)
              Log.d(TAG, it.toString())
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
}
