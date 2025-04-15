package com.wzh.rewriteplay02.base

import android.content.BroadcastReceiver
import android.os.Bundle
import com.wzh.base.view.base.BaseActivity
import com.wzh.rewriteplay02.article.ArticleBroadCast

abstract class ArticleCollectBaseActivity : BaseActivity() {
    private var articleReceiver: BroadcastReceiver? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        articleReceiver = ArticleBroadCast.setArticleChangesReceiver(this) { initData() }
    }

    override fun onDestroy() {
        super.onDestroy()
        ArticleBroadCast.clearArticleChangesReceiver(this, articleReceiver)
    }
}