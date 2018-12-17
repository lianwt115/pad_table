package com.ctd.cymanage.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

class MyViewPager(context: Context, attrs: AttributeSet?):ViewPager(context,attrs) {

    private var isScroll = true

    constructor(context: Context):this(context,null)


    fun  setScroll(scroll:Boolean){
        this.isScroll = scroll
    }


    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return this.isScroll && super.onInterceptTouchEvent(ev)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return this.isScroll && super.onTouchEvent(ev)
    }

}