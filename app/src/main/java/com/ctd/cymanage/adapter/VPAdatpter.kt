package com.ctd.cymanage.adapter

import android.view.View
import android.view.ViewGroup


class VPAdatpter(fm: androidx.fragment.app.FragmentManager, list: ArrayList<androidx.fragment.app.Fragment>, titles : MutableList<String>) : androidx.fragment.app.FragmentStatePagerAdapter(fm) {
    var mFm : androidx.fragment.app.FragmentManager = fm
    var mList : ArrayList<androidx.fragment.app.Fragment> = list
    var mTitles : MutableList<String> = titles

    override fun getItem(position: Int): androidx.fragment.app.Fragment {
        return mList[position]

    }

    override fun getCount(): Int {
        return mList.size
    }

    override fun getPageTitle(position: Int): CharSequence {
        return mTitles[position]
    }

}