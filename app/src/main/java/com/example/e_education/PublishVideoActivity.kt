package com.example.e_education

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.ProgressBar
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.e_education.models.ChaptersViewModel
import com.example.e_education.models.Lecture
import com.example.e_education.models.UploadListener
import com.example.e_education.utils.ActivityIndex
import com.example.e_education.utils.SubjectNumber
import com.example.e_education.utils.toast
import kotlinx.android.synthetic.main.activity_publish_video.*
import java.io.ByteArrayOutputStream
import java.util.regex.Pattern

class PublishVideoActivity : AppCompatActivity(), UploadListener {

    private val TAG = "PublishVideoActivity"
    private lateinit var model: ChaptersViewModel
    private var chapter = Lecture()
    companion object {
        private const val imagePickerRequest = 100
        private val permissionArr = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    }

    override fun onUploadStarted() {
        hideKeyboard(this)
        val layout = LayoutInflater.from(this).inflate(R.layout.upload_progress, null)
        AlertDialog.Builder(this)
            .setView(layout)
            .setCancelable(false)
            .create()
            .show()
        val uploadProgressBar: ProgressBar = layout.findViewById(R.id.uploadProgressBar)
        model.uploadProgress.observe(this, Observer<Long> {
            uploadProgressBar.progress = it.toInt()
        })
        toast("Starting Upload")
        publishButton.isClickable = false
    }

    override fun onUploadComplete() {
        toast("Upload Complete", Toast.LENGTH_SHORT)
        finish()
    }

    override fun onUploadFailed(msg: String) {
        toast(msg)
        publishButton.isClickable = true
    }

    private fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }

    private fun getPhoto(){
        if (permissionGranted()) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, imagePickerRequest)
        } else {
            ActivityCompat.requestPermissions(this, permissionArr, imagePickerRequest)
        }
    }

    private fun permissionGranted(): Boolean{
        return (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish_video)
        title = "Publish a New Video"
        val standard = intent.getStringExtra("standard")
        val subject = intent.getIntExtra("subject", -1)
        model = ViewModelProviders.of(this).get(ChaptersViewModel::class.java)
        model.init(standard, subject)
        model.chapterRepos.uploadListener = this
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

        uploadImage.setOnClickListener {
            Log.d(TAG, "clicked")
            getPhoto()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when(requestCode){
            imagePickerRequest ->
            {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    getPhoto()

                } else {
                    uploadImage.isClickable = false
                }
            }
            else -> return
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK){
            if (requestCode == imagePickerRequest && data != null){

                uploadImage.setImageBitmap(MediaStore.Images.Media.getBitmap(this.contentResolver, data.data))
            }
        }
    }
    fun onRadioButtonClicked(view: View){
        if (view is RadioButton) {
            val checked = view.isChecked

            if (view.id == R.id.radio_existing && checked) {
                newChapterNameField.visibility = View.GONE
                chapterSpinner.visibility = View.VISIBLE
                chapterNum.visibility = View.GONE
                model.getChapterList()?.observe(this, Observer<List<Lecture>> {
                    val list = arrayListOf("Select existing Chapter")
                    for (i in it) {
                        list.add("${i.ofChapter}: ${i.chapterName}")
                    }
                    chapterSpinner.adapter = ArrayAdapter<String>(
                        this,
                        android.R.layout.simple_spinner_dropdown_item, list.distinct()
                    )
                })

            } else if (view.id == radio_newChapter.id && checked) {
                chapterSpinner.visibility = View.GONE
                newChapterNameField.visibility = View.VISIBLE
                chapterNum.visibility = View.VISIBLE
            }
            chapterSpinner.adapter = ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_dropdown_item,
                resources.getStringArray(R.array.classes)
            )
        }
    }

    private fun isFormValid(): Boolean{
        if (youtubeURLField.text.isEmpty())
        {
            toast("Please enter a valid URL!")
            return false
        }
        if (classSpinner.selectedItemPosition == 0){
            toast("Please select a class!", Toast.LENGTH_LONG)
            return false
        }
        if (radio_newChapter.isSelected){
            if (newChapterNameField.text.isEmpty() && !chapterSpinner.isSelected)
            {
                toast("Please enter chapter name or select from an existing one!")
                return false
            }
            if (chapterNum.text.isEmpty()){
                toast("Please enter lecture name!")
                return false
            }
        }
        if (radio_existing.isSelected){
            if (lectureNameField.text.isEmpty())
            {
                toast("Please enter lecture name!")
                return false
            }
            if (chapterSpinner.selectedItemPosition == 0) {
                toast("Please select a chapter in which lecture is to be added!")
                return false
            }
        }
        return true
    }
    fun onPublishButtonClicked(view: View){
        if (isFormValid()){
            if (radio_newChapter.isChecked){
                Log.d(TAG, "Adding New chapter")
                chapter.chapterName = newChapterNameField.text.toString()
                chapter.ofChapter = chapterNum.text.toString().toInt()
            } else if (radio_existing.isChecked){

                val pattern = Pattern.compile("[0-9]+")
                val m = pattern.matcher(chapterSpinner.selectedItem as String)
                m.find()
                chapter.chapterName = (chapterSpinner.selectedItem as String).substring(m.end() + 2)
                val num = (chapterSpinner.selectedItem as String).substring(m.start(), m.end()).toInt()
                chapter.ofChapter = num
            }

            chapter.standard = classSpinner.selectedItem as String
            chapter.id = youtubeURLField.text.toString()
            chapter.lectureName = lectureNameField.text.toString()
            chapter.standard = classSpinner.selectedItem as String
            chapter.subject = SubjectNumber.toKey(subjectSpinner.selectedItem as String)
            val drawable = (uploadImage.drawable) as BitmapDrawable
            val baos = ByteArrayOutputStream()
            val bitmap = drawable.bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()
            model.insert(chapter, data)
        } else {
            return
        }
    }
}
