package com.wzh.base.view.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

interface BaseInit {
    //initData可以用于刷新数据
    fun initData()
    //初始化 RecyclerView、ViewPager 等视图组件
    //设置点击事件监听器
    //开始网络请求或数据库查询
    //绑定 ViewModel 的 LiveData 观察者
    fun initView()
}

interface BaseActivityInit : BaseInit {
    fun getRealLayoutView(): View
}

interface BaseFragmentInit : BaseInit {
    fun getRealLayoutView(inflater: LayoutInflater, container: ViewGroup?, attachToRoot: Boolean): View
}