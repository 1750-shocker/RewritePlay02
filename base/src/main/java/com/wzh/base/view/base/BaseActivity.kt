package com.wzh.base.view.base

import android.app.Activity
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.activity.enableEdgeToEdge
import androidx.annotation.CallSuper
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import com.scwang.smart.refresh.layout.util.SmartUtil.dp2px
import com.wzh.base.R
import com.wzh.base.util.AndroidVersion
import com.wzh.base.util.showToast
import com.wzh.base.util.transparentStatusBar
import com.wzh.base.view.base.lce.DefaultLceImpl
import com.wzh.base.view.base.lce.ILce
import java.lang.ref.WeakReference

abstract class BaseActivity : AppCompatActivity(), ILce, BaseActivityInit {

    companion object {
        private const val TAG = "BaseActivity"
    }

    //Activity中的LCE展示的视图
    private var loading: View? = null
    private var loadErrorView: View? = null
    private var badNetworkView: View? = null
    private var noContentView: View? = null

    //LCE代理类实例
    private var defaultLce: ILce? = null

    //一个弱引用,用于郭霖实践：同意管理多个Activity
    private var weakRefActivity: WeakReference<Activity>? = null

    //注意这是模板，子类实现initView和initData时就是在onCreate中实现
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //版本适配显示状态栏
        if (!AndroidVersion.hasV()) {
            transparentStatusBar()
        } else {
            enableEdgeToEdge()
        }
        //加载视图统一用getLayoutView()方法,这里调用自己扩写的setContentView()方法
        setContentView(getRealLayoutView())
        //用ActivityCollector收集弱引用
        ActivityCollector.add(WeakReference(this))
        weakRefActivity = WeakReference(this)
        //初始化视图和数据
        initView()
        initData()
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        //设置LCE
        setupViews()
    }

    //使用 open 关键字，允许子类重写该方法
    //使用 protected 访问修饰符，限制只能在类内部或子类中访问
    protected open fun setupViews() {
        //加载lce布局文件
        val lceView = View.inflate(this, R.layout.layout_lce, null)
        //根据屏幕方向和是否是搜索页，动态设置视图的上边距
        val params = FrameLayout.LayoutParams(
            FrameLayout.LayoutParams.MATCH_PARENT,
            FrameLayout.LayoutParams.MATCH_PARENT
        )
        val isPort = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT
        params.setMargins(
            0,
            dp2px(
                if (isPort || isSearchPage()
                ) 70f else 55f
            ),
            0,
            0
        )
        addContentView(lceView,params)

        loading = lceView.findViewById(R.id.loading)
        loadErrorView = lceView.findViewById(R.id.load_error_view)
        badNetworkView = lceView.findViewById(R.id.bad_network_view)
        noContentView = lceView.findViewById(R.id.no_content_view)
        if(loading==null) Log.e(TAG, "setupViews: loading is null", )
        if(loadErrorView==null) Log.e(TAG, "setupViews: loadErrorView is null", )
        if(badNetworkView==null) Log.e(TAG, "setupViews: badNetworkView is null", )
        if(noContentView==null) Log.e(TAG, "setupViews: noContentView is null", )
        //创建Lce的代理类实例并调用loadFinished方法（显示默认视图）
        defaultLce = DefaultLceImpl(loading, loadErrorView, badNetworkView, noContentView)
        loadFinished()
    }

    protected open fun isSearchPage(): Boolean {
        return false
    }

    //**数据状态和Lce联动**
    //页面数据是观察livedata获得变化的，根据livedata的状态来更新Lce
    //子类可以通过调用这个方法把自己的livedata传入，数据绑定方法传入一个lambda表达式，用于更新界面
    fun <T> setDataStatus(dataLiveData: LiveData<Result<T>>, onDataStatus: (T) -> Unit) {
        dataLiveData.observe(this) {
            if (it.isSuccess) {
                val dataList = it.getOrNull()
                if (dataList != null) {
                    loadFinished()
                    //lce执行结束，数据请求成功，更新界面
                    onDataStatus(dataList)
                } else {
                    showLoadErrorView()
                }
            } else {
                showToast(getString(R.string.bad_network_view_tip))
                //传入点击监听，点击网络不好的视图重新加载数据
                showBadNetworkView { initData() }
            }
        }
    }
    //用代理的lce实例实现lce接口
    @CallSuper
    override fun startLoading(){
        defaultLce?.startLoading()
    }
    @CallSuper
    override fun loadFinished(){
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

    override fun onDestroy() {
        super.onDestroy()
        ActivityCollector.remove(weakRefActivity)
    }
}