package com.example.e_education

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.e_education.utils.ActivityIndex
import com.example.e_education.models.Chapter
import com.example.e_education.models.ChaptersViewModel
import com.example.e_education.models.Lecture
import com.example.e_education.utils.toast
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_publish_video.*

class PublishVideoActivity : AppCompatActivity(){

    private val TAG = "PublishVideoActivity"
    private lateinit var model: ChaptersViewModel
    private var chapter: Chapter = Chapter()
    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish_video)
        title = "Publish a New Video"
        model = ViewModelProviders.of(this).get(ChaptersViewModel::class.java)
        val parentActivity = intent.getIntExtra("activity", ActivityIndex.ChaptersActivity)
        if (parentActivity == ActivityIndex.ChaptersActivity) {
            radio_newChapter.isChecked = true
            onRadioButtonClicked(radio_newChapter)
        }
        else if (parentActivity == ActivityIndex.LectureActivity) {
            radio_existing.isChecked = true
            onRadioButtonClicked(radio_existing)
        }
        classSpinner.adapter = ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.classes))
        subjectSpinner.adapter = ArrayAdapter<String>(this,
            android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.subjects))
    }


    fun onRadioButtonClicked(view: View){
        if (view.id == R.id.radio_existing) {
            newChapterNameField.visibility = View.GONE
            chapterSpinner.visibility = View.VISIBLE
            model.getChapterList()?.observe(this, Observer<List<Chapter>> {
                val list = arrayListOf("Select existing Chapter")
                for (i in it){
                    list.add(i.chapterName)
                }
                chapterSpinner.adapter = ArrayAdapter<String>(
                    this,
                    android.R.layout.simple_spinner_dropdown_item, list)
            })

        }

        else if (view.id == radio_newChapter.id) {
            chapterSpinner.visibility = View.GONE
            newChapterNameField.visibility = View.VISIBLE
        }
        chapterSpinner.adapter = ArrayAdapter<String>(
            this, android.R.layout.simple_spinner_dropdown_item,
            resources.getStringArray(R.array.classes)
        )
    }

    private fun isFormValid(): Boolean{
        if (newChapterNameField.text.isEmpty() && !chapterSpinner.isSelected)
        {
            toast("Please enter chapter name or select from an existing one!")
            return false
        }
        if (youtubeURLField.text.isEmpty())
        {
            toast("Please enter a valid URL!")
            return false
        }
        if (chapterNum.text.isEmpty()){
            toast("Please enter lecture name!")
            return false
        }
        if (classSpinner.selectedItemPosition == 0){
            toast("Please select a class!", Toast.LENGTH_LONG)
            return false
        }
        if (radio_newChapter.isSelected && chapterSpinner.selectedItemPosition == 0){
                toast("Please select a chapter in which lecture is to be added!")
                return false
        }
        if (radio_existing.isSelected){
            if (lectureNameField.text.isEmpty())
            {
                toast("Please enter lecture name!")
                return false
            }
        }
        return true
    }
    fun onPublishButtonClicked(view: View){
        if (isFormValid()){
            if (radio_newChapter.isSelected){
                chapter.chapterName = newChapterNameField.text.toString()
                chapter.chapterNumber = chapterNum.text.toString().toInt()
            } else if (radio_existing.isSelected){
                chapter.chapterName = (chapterSpinner.selectedItem as TextView).text.toString()
                chapter.chapterNumber = chapterSpinner.selectedItemPosition
            }

            chapter.standard = (classSpinner.selectedView as TextView).text.toString()
            model.insert(chapter, Lecture(youtubeURLField.text.toString(),
                lectureNameField.text.toString(),
                chapterNum.text.toString().toInt(),
                newChapterNameField.text.toString(),
                ((classSpinner.selectedView) as TextView).text.toString(),
                subjectSpinner.selectedItemPosition))
            finish()
        } else {
            return
        }
    }
}
