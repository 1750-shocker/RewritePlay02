package com.wzh.rewriteplay02.main

import android.content.Context
import android.util.AttributeSet
import android.util.Log
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
    //外部调用的init，传入两个关键对象
    fun init(fm: FragmentManager?, viewModel: MainViewModel) {
        mFragmentManager = fm
        mViewModel = viewModel
        //初始化fragment列表
        if (mFragments.isEmpty()) {
            mFragments.apply {
                add(getCurrentFragment(0))
                add(getCurrentFragment(1))
                add(getCurrentFragment(2))
                add(getCurrentFragment(3))
            }
        }
        //调用自己写的事务，把当前位置传入
        changeFragment(viewModel.getPage()?:0)
    }

    /**
     * 实现fragment的切换
     * 通过当前position，调用fragmentManager的事务，把当前fragment替换成目标fragment
     * 并更新position
     */
    protected open fun changeFragment(position: Int) {
        Log.e("wocaonima", "changeFragment: ${position}")
        mViewModel.setPage(position)
        //根据position获取目标fragment
        val targetFg: Fragment = mFragments[position]
        //执行事务：先隐藏当前fragment，再显示目标fragment
        mFragmentManager?.beginTransaction()?.apply{
            currentFragment?.apply{
                hide(this)
            }
            //在需要频繁切换 Fragment 的场景中优化 Fragment 的添加、移除、显示和隐藏操作的执行顺序
            setReorderingAllowed(true)
            if (!targetFg.isAdded) {
                add(R.id.fl_content, targetFg).commit()
            }else{
                show(targetFg).commit()
            }
        }
        currentFragment = targetFg
    }

    /**
     * 根据提供的position，返回对应的fragment
     * 这里对成员变量fragment的调用会触发他们的懒加载
     * 所以可以在fragment的伴生对象中，新建一个newInstance方法，返回对应的fragment
     */
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
