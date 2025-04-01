package com.wzh.base.view.base.lce

import android.view.View

interface ILce {
    //实现showLoading()和loadFinished()方法要用@CallSuper标注，
    // 表示子类可以重写这两个方法，但是必须调用父类的方法（父类实现很重要，不要覆盖）
    fun startLoading()
    fun loadFinished()
    //TODO:这里默认显示tip有个多语言适配，从资源文件获取字符串
    fun showLoadErrorView(tip:String="Failed to load data")
    fun showBadNetworkView(listener: View.OnClickListener)
    fun showNoContentView(tip:String)
}