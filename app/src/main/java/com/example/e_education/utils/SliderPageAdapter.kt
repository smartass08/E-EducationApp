package com.example.e_education.utils

import android.content.Context
import android.os.Handler
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.Toast
import com.example.e_education.R
import java.util.concurrent.Executors


class SliderPageAdapter(private var context: Context, private var imageArray: ArrayList<Int>): PagerAdapter()
{
    private var layoutInflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
    var handler: Handler? = null
    var runTask: Runnable? = null
    private var currentPage: Int = 0

    fun onActivityDestroyed(){
        val service = Executors.newSingleThreadExecutor()
        val f = service.submit(runTask)
        f.cancel(true)
    }
    fun setAutoSlideDuration(viewPager: ViewPager, duration: Long){
        handler = Handler()
        runTask = object: Runnable{
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


    override fun getCount(): Int = imageArray.size

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val itemView = layoutInflater.inflate(R.layout.recent_uploads_object_layout, container, false)
        val imageView = itemView.findViewById<ImageView>(R.id.sliderImage)
        imageView.setImageResource(imageArray[position])
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