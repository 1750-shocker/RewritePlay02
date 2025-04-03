package com.wzh.rewriteplay02.base

import android.content.BroadcastReceiver
import android.os.Bundle
import android.view.View
import com.wzh.base.view.base.BaseFragment
import com.wzh.rewriteplay02.article.ArticleBroadCast

abstract class ArticleCollectBaseFragment : BaseFragment() {
    private var articleReceiver: BroadcastReceiver? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //TODO:注册广播
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //TODO:注销广播
    }

    override fun onResume() {
        super.onResume()
        refreshData()
    }
    abstract fun refreshData()
}