package com.cobeisfresh.azil.ui.main.pager

import android.support.v4.app.Fragment
import android.support.v4.app.FragmentManager
import android.support.v4.app.FragmentPagerAdapter
import java.util.*

/**
 * Created by Zerina Salitrezic on 11/01/2018.
 */

class MainPagerAdapter(fm: FragmentManager) : FragmentPagerAdapter(fm) {

    private val fragments = ArrayList<Fragment>()

    override fun getItem(position: Int): Fragment = fragments[position]

    override fun getCount(): Int = fragments.size

    fun addFragment(position: Int, fragment: Fragment) {
        if (position > fragments.size) {
            fragments.add(fragment)
        } else {
            fragments.add(position, fragment)
        }
        notifyDataSetChanged()
    }
}