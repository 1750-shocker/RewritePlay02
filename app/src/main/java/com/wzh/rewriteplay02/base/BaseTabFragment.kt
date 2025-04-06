package com.wzh.rewriteplay02.base

import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.wzh.base.view.base.BaseFragment

abstract class BaseTabFragment : BaseFragment(),
    TabLayout.OnTabSelectedListener {



    override fun onTabUnselected(tab: TabLayout.Tab?) {
    }

    override fun onTabReselected(tab: TabLayout.Tab?) {
    }



    override fun onTabSelected(tab: TabLayout.Tab?) {
        if (tab != null && tab.position > 0)
            onTabPageSelected(tab.position)
    }

    abstract fun onTabPageSelected(position: Int)

}