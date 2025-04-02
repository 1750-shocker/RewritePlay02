package com.wzh.rewriteplay02.main

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.wzh.rewriteplay02.R
import com.wzh.rewriteplay02.project.ProjectFragment

abstract class BaseHomeBottomTabWidget(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr), View.OnClickListener {
    //接受外面传进来的FragmentManager和viewModel
    private var mFragmentManager: FragmentManager? = null
    private lateinit var mViewModel: MainViewModel

    private var mFragments: ArrayList<Fragment> = arrayListOf()
    private var currentFragment: Fragment? = null
    //在这里自己新建所有主页面的Fragment
    private val mProjectFragment: ProjectFragment by lazy { ProjectFragment.newInstance() }

    //外部调用的init
    fun init(fm: FragmentManager?, viewModel: MainViewModel) {
        mFragmentManager = fm
        mViewModel = viewModel
        if (mFragments.isEmpty()) {
            mFragments.apply {
                add(getCurrentFragment(0))
                add(getCurrentFragment(1))
                add(getCurrentFragment(2))
                add(getCurrentFragment(3))
            }
        }
        fragmentManagerFun(viewModel.getPage()?:0)
    }

    /**
     * 实现fragment的切换
     */
    protected open fun fragmentManagerFun(position: Int) {
        mViewModel.setPage(position)
        val targetFg: Fragment = mFragments[position]
        mFragmentManager?.beginTransaction()?.apply{
            currentFragment?.apply{
                hide(this)
            }
            setReorderingAllowed(true)
            if (!targetFg.isAdded) {
                add(R.id.fl_content, targetFg).commit()
            }else{
                show(targetFg).commit()
            }
        }
        currentFragment = targetFg
    }

    private fun getCurrentFragment(position: Int): Fragment {
        return when(position){
            0 -> mProjectFragment
            else -> mProjectFragment
        }
    }
    open fun destroy(){
        mFragmentManager?.apply{
            if (!isDestroyed) {
                mFragmentManager = null
            }
            if (mFragments.isNotEmpty()) {
                mFragments.clear()
            }
        }
    }
}
