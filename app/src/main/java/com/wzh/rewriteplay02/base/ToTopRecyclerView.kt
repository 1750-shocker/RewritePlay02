package com.wzh.rewriteplay02.base

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.scwang.smart.refresh.layout.SmartRefreshLayout
import com.wzh.base.view.custom.StaggeredDividerItemDecoration
import com.wzh.rewriteplay02.databinding.LayoutToTopRecyclerBinding
import kotlin.system.measureTimeMillis

class ToTopRecyclerView @JvmOverloads constructor(
    private val mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(mContext, attrs, defStyleAttr), View.OnClickListener {
    private lateinit var mSmartRefreshLayout: SmartRefreshLayout
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mToTopIcon: ImageView
    private val mLoadTime = 1000

    init {
        initView()
    }
    override fun onClick(v: View?) {
        mRecyclerView.smoothScrollToPosition(0)
    }

    private fun initView() {
        val binding = LayoutToTopRecyclerBinding.inflate(LayoutInflater.from(context), this, true)
        binding.apply {
            mSmartRefreshLayout = smartRefreshLayout
            mRecyclerView = rvToTop
            mToTopIcon = ivToTop
            mToTopIcon.setOnClickListener(this@ToTopRecyclerView)
            //设置上滑显示指定按钮
            mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (!recyclerView.canScrollVertically(-1))
                        mToTopIcon.visibility = View.GONE
                    else if (dy < 0)
                        mToTopIcon.visibility = View.VISIBLE
                    else if (dy > 0)
                        mToTopIcon.visibility = View.GONE
                }
            })
        }
    }

    fun setAdapter(adapter: RecyclerView.Adapter<*>) {
        adapter.setHasStableIds(true)
        mRecyclerView.adapter = adapter
    }

    fun setOnRefreshListener(onRefreshListener: () -> Unit, onLoadMoreListener: () -> Unit) {
        mSmartRefreshLayout.apply {
            setOnRefreshListener { reLayout ->
                reLayout.finishRefresh(measureTimeMillis {
                    onRefreshListener.invoke()
                }.toInt())
            }
            setOnLoadMoreListener { reLayout ->
                val time = measureTimeMillis {
                    onLoadMoreListener.invoke()
                }.toInt()
                reLayout.finishLoadMore(if (time > mLoadTime) time else mLoadTime)
            }
        }
    }

    /**
     * 横竖屏适配显示列表
     */
    fun setRecyclerViewLayoutManager(isLinearLayout:Boolean){
        if (isLinearLayout) {
            mRecyclerView.layoutManager = LinearLayoutManager(context)
        }else{
            val spanCount=2
            val layoutManager = StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL)
            mRecyclerView.layoutManager = layoutManager
            layoutManager.gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
            mRecyclerView.addItemDecoration(StaggeredDividerItemDecoration(context))
            mRecyclerView.addOnScrollListener(object:RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    val first = IntArray(spanCount)
                    layoutManager.findFirstVisibleItemPositions(first)
                    if(newState==RecyclerView.SCROLL_STATE_IDLE&&(first[0]==1||first[1]==1)){
                        layoutManager.invalidateSpanAssignments()
                    }
                }
            })
        }
    }
}