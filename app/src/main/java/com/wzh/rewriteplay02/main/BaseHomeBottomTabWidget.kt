package com.wzh.rewriteplay02.main

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.wzh.rewriteplay02.project.ProjectFragment

abstract class BaseHomeBottomTabWidget(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {
    private var mFragmentManager: FragmentManager? = null
    private var mFragments: ArrayList<Fragment> = arrayListOf()
    private lateinit var mViewModel: MainViewModel
    private var currentFragment: Fragment? = null
    private val mProjectFragment: ProjectFragment by lazy { ProjectFragment.newInstance() }


    private fun getCurrentFragment(position: Int): Fragment {
        return when(position){
            0 -> mProjectFragment
            else -> mProjectFragment
        }
    }
}
