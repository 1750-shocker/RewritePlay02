package com.wzh.rewriteplay02.profile.share

import android.view.View
import androidx.activity.viewModels
import com.wzh.base.view.base.BaseActivity
import com.wzh.rewriteplay02.article.ArticleAdapter
import com.wzh.rewriteplay02.base.ArticleCollectBaseActivity
import com.wzh.rewriteplay02.databinding.ActivityArticleBinding
import com.wzh.rewriteplay02.databinding.ActivityShareBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ShareActivity : ArticleCollectBaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityShareBinding
    private val viewModel by viewModels<ShareViewModel>()
    private var isMine: Boolean = true
    private var userId: Int = 0
    private lateinit var articleAdapter: ArticleAdapter
    private var page = 1

    override fun getRealLayoutView(): View {
        TODO("Not yet implemented")
    }

    override fun initView() {
        TODO("Not yet implemented")
    }

    override fun onClick(v: View?) {
        TODO("Not yet implemented")
    }

}