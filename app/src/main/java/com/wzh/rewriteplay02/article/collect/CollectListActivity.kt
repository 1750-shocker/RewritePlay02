package com.wzh.rewriteplay02.article.collect

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.wzh.rewriteplay02.base.BaseListActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class CollectListActivity : BaseListActivity() {
    private val viewModel by viewModels<CollectListViewModel>()
    private lateinit var articleAdapter: CollectAdapter
    override fun initData() {
        super.initData()
        binding.baseListTitleBar.setTitle(getString(com.wzh.base.R.string.my_collection))
        setDataStatus(viewModel.dataLiveData) {
            if (page == 1 && viewModel.dataList.size > 0) {
                viewModel.dataList.clear()
            }
            viewModel.dataList.addAll(it.datas)
            articleAdapter.notifyItemInserted(it.datas.size)
        }
    }

    override fun initView() {
//        super.initView()
        //BUG：收藏列表下拉刷新会（玩安卓会崩溃）（我的会闪现首页并覆盖当前页）
        binding.baseListToTop.setRecyclerViewLayoutManager(resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT)
        binding.baseListToTop.setOnRefreshListener({
            page = 1
            getDataList()
        }, {
//            page++
//            getDataList()
        })

        articleAdapter = CollectAdapter(this, viewModel.dataList, lifecycleScope)
        binding.baseListToTop.setAdapter(articleAdapter)
    }

    override fun isStaggeredGrid(): Boolean {
        return true
    }

    override fun getDataList() {
        if (viewModel.dataList.size <= 0) startLoading()
        viewModel.getDataList(page)
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, CollectListActivity::class.java)
            context.startActivity(intent)
        }
    }
}