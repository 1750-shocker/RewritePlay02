package com.wzh.rewriteplay02.base

import android.content.res.Configuration
import android.view.View
import com.wzh.rewriteplay02.databinding.ActivityBaseListBinding


abstract class BaseListActivity : ArticleCollectBaseActivity() {

    protected lateinit var binding: ActivityBaseListBinding
    protected var page = 1

    override fun getRealLayoutView(): View {
        binding = ActivityBaseListBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initData() {
        getDataList()
    }

    abstract fun getDataList()

    override fun initView() {
        binding.baseListToTop.setRecyclerViewLayoutManager(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
        binding.baseListToTop.setOnRefreshListener({
            page = 1
            getDataList()
        }, {
            page++
            getDataList()
        })
    }

    abstract fun isStaggeredGrid(): Boolean

}
