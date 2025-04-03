package com.wzh.rewriteplay02.base

import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.wzh.rewriteplay02.article.ArticleAdapter
import com.wzh.rewriteplay02.databinding.FragmentBaseListBinding

abstract class BaseListFragment:ArticleCollectBaseFragment() {
    protected var binding: FragmentBaseListBinding?=null
    //ArticleAdapter的初始化由子类完成，因为不同的fragment的adapter可能不同
    protected lateinit var articleAdapter:ArticleAdapter
    protected var page=1
    override fun getRealLayoutView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): View {
        binding = FragmentBaseListBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun initView() {
        binding?.apply {
            //这个listfragment只包含一个自定义的recyclerview,调用其自定义的方法对其进行配置
            baseFragmentToTop.setRecyclerViewLayoutManager(resources.configuration.orientation== Configuration.ORIENTATION_PORTRAIT)
            baseFragmentToTop.setAdapter(articleAdapter)
            baseFragmentToTop.setOnRefreshListener(
                onRefreshListener = {
                    page=1
                    refreshData()
                },
                onLoadMoreListener = {
                    page++
                    refreshData()
                }
            )
        }
    }
}