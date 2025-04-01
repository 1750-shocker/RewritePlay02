package com.wzh.base.view.base.lce

import android.view.View
import android.widget.TextView
import com.wzh.base.R


class DefaultLceImpl(
    private val loading: View?,
    private val loadErrorView: View?,
    private val badNetworkView: View?,
    private val noContentView: View?
) : ILce {
    //LCE展示的视图,其本质就是在容器中根据数据加载结果显示不同的视图，如果
// 数据加载成功，显示原本的视图，否则显示LCE视图,通过在frameLayout中
//叠加视图并控制其可见性来实现
    override fun showLoading() {
        //这个方法就是关掉所有lce视图
        loadFinished()
        loading?.visibility = View.VISIBLE
    }

    override fun loadFinished() {
        loading?.visibility = View.GONE
        loadErrorView?.visibility = View.GONE
        badNetworkView?.visibility = View.GONE
        noContentView?.visibility = View.GONE
    }

    override fun showLoadErrorView(tip: String) {
        loadFinished()
        val loadErrorText = loadErrorView?.findViewById<TextView>(R.id.load_error_text)
        loadErrorText?.text = tip
        loadErrorView?.visibility = View.VISIBLE
    }

    override fun showBadNetworkView(listener: View.OnClickListener) {
        loadFinished()
        badNetworkView?.visibility = View.VISIBLE
        badNetworkView?.setOnClickListener(listener)
    }

    override fun showNoContentView(tip: String) {
        loadFinished()
        val noContentText = noContentView?.findViewById<TextView>(R.id.no_content_text)
        noContentText?.text = tip
        noContentView?.visibility = View.VISIBLE
    }
}