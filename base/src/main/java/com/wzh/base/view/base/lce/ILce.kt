package com.wzh.base.view.base.lce

import android.view.View
import com.wzh.base.Play

interface ILce {
    //实现showLoading()和loadFinished()方法要用@CallSuper标注，
    // 表示子类可以重写这两个方法，但是必须调用父类的方法（父类实现很重要，不要覆盖）
    fun startLoading()
    fun loadFinished()

    fun showLoadErrorView(
        tip: String = Play.context?.getString(com.wzh.base.R.string.failed_load_data)
            ?: "Fialed to load data"
    )

    fun showBadNetworkView(listener: View.OnClickListener)
    fun showNoContentView(tip: String)
}