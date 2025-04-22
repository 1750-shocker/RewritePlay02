package com.wzh.rewriteplay02.profile.history

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import com.wzh.base.util.showToast
import com.wzh.rewriteplay02.article.ArticleAdapter
import com.wzh.rewriteplay02.base.BaseListActivity
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class BrowseHistoryActivity : BaseListActivity() {

    private val viewModel by viewModels<BrowseHistoryViewModel>()
    private lateinit var articleAdapter: ArticleAdapter

    override fun initView() {
        super.initView()
        articleAdapter = ArticleAdapter(
            this,
            viewModel.dataList,
            false
        )
        binding.baseListToTop.setAdapter(articleAdapter)
        binding.baseListTitleBar.setTitle(getString(com.wzh.base.R.string.browsing_history))
    }

    override fun isStaggeredGrid(): Boolean {
        return true
    }

    override fun getDataList() {
        if (viewModel.dataList.size <= 0) {
            startLoading()
        }
        viewModel.getDataList(page)
    }

    override fun initData() {
        super.initData()
        viewModel.dataLiveData.observe(this) {
            if (it.isSuccess) {
                val articleList = it.getOrNull()
                if (articleList != null) {
                    loadFinished()
                    if (page == 1 && viewModel.dataList.size > 0) {
                        viewModel.dataList.clear()
                    }
                    viewModel.dataList.addAll(articleList)
                    articleAdapter.notifyItemInserted(articleList.size)
                } else {
                    showLoadErrorView()
                }
            } else {
                if (viewModel.dataList.size <= 0) {
                    showNoContentView(getString(com.wzh.base.R.string.no_browsing_history))
                } else {
                    showToast(getString(com.wzh.base.R.string.no_more_data))
                    loadFinished()
                }
            }
        }
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, BrowseHistoryActivity::class.java)
            context.startActivity(intent)
        }
    }

}