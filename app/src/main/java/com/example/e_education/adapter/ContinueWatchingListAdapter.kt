package com.example.e_education.adapter

import android.app.AlertDialog
import android.content.Context
import android.graphics.Point
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.e_education.R
import com.example.e_education.models.ContinueWatchingData

class ContinueWatchingListAdapter(private val context: Context, private val data: ArrayList<ContinueWatchingData>) :
    RecyclerView.Adapter<ContinueWatchingListAdapter.mViewHolder>() {
    private var itemsPerPage = 2 // TODO: Find a better name for itemsPerPage

    public fun setItemsPerPage(value: Int) {
        // Call before attaching adapter to the RecyclerView. Default value is 2
        itemsPerPage = value
    }

    private fun getScreenWidth(): Int {

        val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = wm.defaultDisplay
        val size = Point()
        display.getSize(size)

        return size.x - 24
    }

    class mViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val captionTextView: TextView = view.findViewById(R.id.captionTextView)
        val lectureImageView: ImageView = view.findViewById(R.id.lectureImage)
        val infoIcon: ImageView = view.findViewById(R.id.infoIcon)
    }

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): mViewHolder {
        val viewHolder = LayoutInflater.from(p0.context).inflate(R.layout.continue_watching_recycler_layout, p0, false)
        viewHolder.layoutParams.width = getScreenWidth() / itemsPerPage
        return mViewHolder(viewHolder)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: mViewHolder, at: Int) {
        holder.captionTextView.text = data[at].caption
        holder.lectureImageView.setImageResource(data[at].image)

        // Adding onClickListener
        holder.lectureImageView.setOnClickListener {
            Toast.makeText(context, "Not Implemented", Toast.LENGTH_LONG).show()
        }
        holder.infoIcon.setOnClickListener {
            AlertDialog.Builder(context, android.R.style.Theme_DeviceDefault_Dialog_Alert)
                .setCancelable(true)
                .setIcon(android.R.drawable.ic_dialog_info)
                .setTitle(R.string.author)
                .setMessage("This video is made by Shivam")
                .create()
                .show()
        }
    }
}