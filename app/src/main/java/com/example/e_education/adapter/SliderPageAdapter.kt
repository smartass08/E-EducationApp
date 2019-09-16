package com.example.e_education.adapter

import android.content.Context
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.example.e_education.R
import com.example.e_education.models.SliderData
import java.util.concurrent.Executors


class SliderPageAdapter(private var context: Context, private var sliderDataArray: ArrayList<SliderData>) :
    PagerAdapter() {
    private var layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    var handler: Handler? = null
    var runTask: Runnable? = null
    private var currentPage: Int = 0

    fun onActivityDestroyed() {
        val service = Executors.newSingleThreadExecutor()
        val f = service.submit(runTask)
        f.cancel(true)
    }

    fun setAutoSlideDuration(viewPager: ViewPager, duration: Long) {
        handler = Handler()
        runTask = object : Runnable {
            override fun run() {
                handler!!.postDelayed(this, duration)
                if (currentPage == count)
                    currentPage = 0

                viewPager.currentItem = currentPage++
            }
        }
        runTask!!.run()
    }

    override fun isViewFromObject(p0: View, p1: Any): Boolean {
        return p0 == (p1 as FrameLayout)
    }


    override fun getCount(): Int = sliderDataArray.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = layoutInflater.inflate(R.layout.recent_uploads_object_layout, container, false)
        val imageView = itemView.findViewById<ImageView>(R.id.sliderImage)
        imageView.setImageResource(sliderDataArray[position].image)
        container.addView(itemView)
        imageView.setOnClickListener {
            Toast.makeText(context, "Not implemented", Toast.LENGTH_LONG).show()
        }
        return itemView
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {

        container.removeView(`object` as FrameLayout)
    }
}