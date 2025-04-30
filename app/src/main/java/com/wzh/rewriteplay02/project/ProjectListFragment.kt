package com.wzh.rewriteplay02.project

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.viewModels
import com.wzh.model.pojo.QueryArticle
import com.wzh.rewriteplay02.article.ArticleAdapter
import com.wzh.rewriteplay02.base.BaseListFragment
import dagger.hilt.android.AndroidEntryPoint

private const val PROJECT_CID = "PROJECT_CID"

@AndroidEntryPoint
class ProjectListFragment : BaseListFragment() {
    private val viewModel by viewModels<ProjectListViewModel>()

    //一个tab对应一个projectListFragment，这个碎片放在ViewPager2中
    private var projectCid: Int? = null

    companion object {
        //构造这个碎片时必须传进cid参数用来指示加载哪些数据
        @JvmStatic
        fun newInstance(cid: Int) = ProjectListFragment().apply {
            arguments = Bundle().apply {
                putInt(PROJECT_CID, cid)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            projectCid = it.getInt(PROJECT_CID)
        }
    }

    override fun initView() {
        //把适配器的数据源设置为viewModel中的dataList，之后会对这个集合进行更新
        articleAdapter = ArticleAdapter(requireContext(), viewModel.dataList)
        super.initView()
    }


    override fun initData() {
        //在初始化数据的方法里设置lce
        setDataStatus(viewModel.dataLiveData,
            {
                //网络不行执行
                if (viewModel.dataList.size > 0) loadFinished()
            }) {
            //数据请求正常后进行填充
            if (page == 1 && viewModel.dataList.size > 0) {
                val temp = viewModel.dataList.size
                viewModel.dataList.clear()
                articleAdapter.notifyItemRangeRemoved(0, temp)
            }
            val startPosition = viewModel.dataList.size
            viewModel.dataList.addAll(it)
            //填充完通知更新视图
            articleAdapter.notifyItemRangeInserted(startPosition, it.size)
        }
        getArticleList(false)
    }

    override fun refreshData() {
        getArticleList(true)
    }

    private fun getArticleList(isRefresh: Boolean) {
        if (viewModel.dataList.size <= 0) {
            startLoading()
            projectCid?.apply {
                //参数有可能为空
                viewModel.getDataList(QueryArticle(page, this, isRefresh))
            }
        }
    }

    override fun isHaveHeadMargin(): Boolean {
        return false
    }
}