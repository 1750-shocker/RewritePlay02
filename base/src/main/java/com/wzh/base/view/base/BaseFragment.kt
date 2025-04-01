package com.wzh.base.view.base

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import com.scwang.smart.refresh.layout.util.SmartUtil.dp2px
import com.wzh.base.R
import com.wzh.base.util.showToast
import com.wzh.base.view.base.lce.DefaultLceImpl
import com.wzh.base.view.base.lce.ILce

abstract class BaseFragment : Fragment(), ILce, BaseFragmentInit {
    //LCE展示的视图
    private var loading: View? = null
    private var loadErrorView: View? = null
    private var badNetworkView: View? = null
    private var noContentView: View? = null

    //LCE代理类实例
    private var defaultLce: ILce? = null

    protected open fun isHaveHeadMargin(): Boolean {
        return true
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //这个方法用来创建Fragment视图
        //这个frameLayout是一个容器，用于承载Fragment的视图
        val frameLayout = FrameLayout(requireContext())
        //初始化LCE视图组件
        val lceView = View.inflate(context, R.layout.layout_lce, null)
        //根据屏幕方向和是否是搜索页，动态设置视图的上边距
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        val isPort = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        params.setMargins(
            0,
            if (isHaveHeadMargin()) dp2px(if (isPort) 70f else 55f)
            else 0,
            0,
            0
        )
        lceView.layoutParams = params
        //创建Lce的代理类实例并调用loadFinished方法（显示默认视图）
        loading = lceView.findViewById(R.id.loading)
        loadErrorView = lceView.findViewById(R.id.load_error_view)
        badNetworkView = lceView.findViewById(R.id.bad_network_view)
        noContentView = lceView.findViewById(R.id.no_content_view)
        if (loading == null) throw NullPointerException("loading is null")
        if (loadErrorView == null) throw NullPointerException("loadErrorView is null")
        if (badNetworkView == null) throw NullPointerException("badNetworkView is null")
        if (noContentView == null) throw NullPointerException("noContentView is null")
        //创建Lce的代理类实例并调用loadFinished方法（显示默认视图）
        defaultLce = DefaultLceImpl(loading, loadErrorView, badNetworkView, noContentView)
        //获取Fragment的布局视图
        val content = getRealLayoutView(inflater, container, false)
        frameLayout.addView(content)
        //将lce视图叠放在上面
        frameLayout.addView(lceView)
        return frameLayout
    }

    //在 Fragment 的视图创建完成后调用
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initData()
    }

    //比BaseActivity多一个onBadNetwork的回调
    fun <T> setDataStatus(
        dataLiveData: LiveData<Result<T>>,
        onBadNetwork: () -> Unit = {},
        onDataStatus: (T) -> Unit
    ) {
        dataLiveData.observe(this) {
            if (it.isSuccess) {
                val dataList = it.getOrNull()
                if (dataList != null) {
                    loadFinished()
                    onDataStatus(dataList)
                } else {
                    showLoadErrorView()
                }
            } else {
                context?.showToast(getString(R.string.bad_network_view_tip))
                //设置网络异常视图的点击事件是重新加载
                showBadNetworkView { initData() }
                onBadNetwork.invoke()
            }
        }
    }

    //以下是ILce的实现由DefaultLceImpl代理
    @CallSuper
    override fun startLoading() {
        defaultLce?.startLoading()
    }

    @CallSuper
    override fun loadFinished() {
        defaultLce?.loadFinished()
    }

    override fun showLoadErrorView(tip: String) {
        defaultLce?.showLoadErrorView(tip)
    }

    override fun showBadNetworkView(listener: View.OnClickListener) {
        defaultLce?.showBadNetworkView(listener)
    }

    override fun showNoContentView(tip: String) {
        defaultLce?.showNoContentView(tip)
    }
}