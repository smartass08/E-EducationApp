package com.example.e_education

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.SpinnerAdapter
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.example.e_education.utils.Activity
import com.example.e_education.viewmodel.ChaptersViewModel
import com.jakewharton.rxbinding2.widget.checked
import kotlinx.android.synthetic.main.activity_publish_video.*

class PublishVideoActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    private lateinit var model: ChaptersViewModel


    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish_video)
        title = "Publish a New Video"
        model = ViewModelProviders.of(this).get(ChaptersViewModel::class.java)
        val parentActivity = intent.getIntExtra("activity", Activity.ChaptersActivity)
        if (parentActivity == Activity.ChaptersActivity) {
            radio_newChapter.isChecked = true
            onRadioButtonClicked(radio_newChapter)
        }
        else if (parentActivity == Activity.LectureActivity) {
            radio_existing.isChecked = true
            onRadioButtonClicked(radio_existing)
        }
    }


    fun onRadioButtonClicked(view: View){
        if (view.id == R.id.radio_existing) {
            newChapterNameField.visibility = View.GONE
            chapterSpinner.visibility = View.VISIBLE
            val items = ArrayList<String>()
            for (item in model.getChapterList()!!.value!!)
                items.add(item.chapterName!!)
            chapterSpinner.adapter = ArrayAdapter<String>(
                this,
                android.R.layout.simple_spinner_dropdown_item, items)
        }

        else if (view.id == radio_newChapter.id) {
            chapterSpinner.visibility = View.GONE
            newChapterNameField.visibility = View.VISIBLE
        }
    }

    fun onPublishButtonClicked(view: View){
        if (newChapterNameField.text.isEmpty() && !chapterSpinner.isSelected)
        {
            Toast.makeText(this, "Please enter chapter name or select from an existing one!", Toast.LENGTH_LONG).show()
            return
        }
        if (youtubeURLField.text.isEmpty())
        {
            Toast.makeText(this, "Please enter a valid URL!", Toast.LENGTH_LONG).show()
            return
        }
        if (lectureNameField.text.isEmpty())
        {
            Toast.makeText(this, "Please enter lecture name!", Toast.LENGTH_LONG).show()
            return
        }
        else {
            Toast.makeText(this, "This function is not yet implemented", Toast.LENGTH_LONG).show()
        }
    }
}
