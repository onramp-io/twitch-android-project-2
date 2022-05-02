package com.example.voyagerx.adapters

import android.R
import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import coil.load


class LaunchCarouselAdapter(private val mContext: Context, private val imageList: List<String?>?) : PagerAdapter() {

    override fun getCount(): Int {
        return imageList?.size ?: 0
    }

    override fun isViewFromObject(view: View, carouselView: Any): Boolean {
        return view === carouselView
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageView = ImageView(mContext)
        imageView.scaleType = ImageView.ScaleType.FIT_CENTER
        imageView.load(imageList?.elementAt(position))
        container.addView(imageView, 0)
        return imageView
    }

    override fun destroyItem(container: ViewGroup, position: Int, carouselView: Any) {
        container.removeView(carouselView as ImageView)
    }
}
