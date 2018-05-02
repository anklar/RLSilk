package com.example.ravi.rocketleaguecompanion.custom


import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import com.example.ravi.rocketleaguecompanion.fragments.GraphFragment
import com.example.ravi.rocketleaguecompanion.fragments.PlayerFragment

class SampleAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    override fun getItem(position: Int): Fragment? = when (position) {
        0 -> PlayerFragment.newInstance()
        1 -> PlayerFragment.newInstance()
        2 -> GraphFragment.newInstance()
        else -> null
    }

    override fun getPageTitle(position: Int): CharSequence = when (position) {
        0 -> "Tab 1 Item"
        1 -> "Tab 2 Item"
        2 -> "Tab 3 Item"
        else -> ""
    }

    override fun getCount(): Int = 3
}