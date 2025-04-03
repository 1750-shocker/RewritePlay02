package com.wzh.base.view.custom

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class FragmentVp2Adapter(mFragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(mFragmentManager, lifecycle) {

    private val mFragment: MutableList<Fragment> = ArrayList()
    private lateinit var mTitles: Array<String>

    override fun getItemCount(): Int {
        return mFragment.size
    }

    override fun createFragment(position: Int): Fragment {
        return mFragment[position]
    }

    fun reset(fragments: List<Fragment>?) {
        fragments?.apply {
            mFragment.clear()
            mFragment.addAll(this)
        }
    }

    fun reset(titles: Array<String>) {
        mTitles = titles
    }

    fun title(position: Int): String {
        return mTitles[position]
    }
}