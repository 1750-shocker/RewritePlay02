package com.wzh.rewriteplay02.article

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.KeyEvent
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.wzh.base.util.getHtmlText
import com.wzh.base.util.showToast
import com.wzh.base.view.base.BaseActivity
import com.wzh.model.model.CollectX
import com.wzh.model.room.entity.Article
import com.wzh.rewriteplay02.R
import com.wzh.rewriteplay02.databinding.ActivityArticleBinding
import com.wzh.rewriteplay02.profile.share.ShareActivity
import dagger.hilt.android.AndroidEntryPoint


const val PAGE_NAME = "PAGE_NAME"
const val PAGE_URL = "PAGE_URL"
const val PAGE_ID = "PAGE_ID"
const val ORIGIN_ID = "ORIGIN_ID"
const val USER_ID = "USER_ID"
const val IS_COLLECTION = "IS_COLLECTION"
@AndroidEntryPoint
class ArticleActivity : BaseActivity(), View.OnClickListener {
    private lateinit var binding: ActivityArticleBinding
    private val viewModel by viewModels<ArticleViewModel>()

    private var pageName=""
    private var pageUrl=""
    private var pageId=-1
    private var originId=-1
    private var userId= -1
    private var isCollection =-1
    private lateinit var bottomDialogIvCollect: ImageView
    private lateinit var bottomDialogTvCollect: TextView
    private var bottomSheetDialog: BottomSheetDialog?=null

    override fun getRealLayoutView(): View {
        binding = ActivityArticleBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun initData() {
        pageName = intent.getStringExtra(PAGE_NAME)?:""
        pageUrl = intent.getStringExtra(PAGE_URL)?:""
        pageId = intent.getIntExtra(PAGE_ID,-1)
        isCollection = intent.getIntExtra(IS_COLLECTION, -1)
        originId = intent.getIntExtra(ORIGIN_ID,-1)
        userId = intent.getIntExtra(USER_ID, -1)
        binding.articleTxtTitle.text = getHtmlText(pageName)
        binding.articleWebView.loadUrl(pageUrl)
    }

    override fun initView() {
        binding.articleImgBack.setOnClickListener(this)
        binding.articleImgRight.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.articleImgBack -> {
                if (binding.articleWebView.canGoBack()) {
                    binding.articleWebView.goBack()
                    return
                } else {
                    finish()
                }
            }

            R.id.articleImgRight -> {
                showBottomDialog()
            }

            R.id.bottomDialogLlCollect -> {
                bottomSheetDialog?.dismiss()
                TODO(viewModel.setCollect())
            }

            R.id.bottomDialogLlCopy -> {
                bottomSheetDialog?.dismiss()
                ArticleUtils.copyToClipboard(this, pageUrl)
                showToast(getString(com.wzh.base.R.string.copy_succeeded))
            }
            R.id.bottomDialogLlBrowser -> {
                bottomSheetDialog?.dismiss()
                ArticleUtils.jumpBrowser(this, pageUrl)
            }
            R.id.bottomDialogLlShare -> {
                bottomSheetDialog?.dismiss()
                ArticleUtils.shareUrl(this, pageName, pageUrl)
            }
            R.id.bottomDialogLlDynamic -> {
                bottomSheetDialog?.dismiss()
                ShareActivity.actionStart(this, false, userId)
            }
            R.id.bottomDialogLlReload -> {
                bottomSheetDialog?.dismiss()
                binding.articleWebView.reload()
            }

            R.id.bottomDialogTvCancel -> {
                bottomSheetDialog?.dismiss()
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode==KeyEvent.KEYCODE_BACK && binding.articleWebView.canGoForward()){
            binding.articleWebView.goBack()
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private fun showBottomDialog(){
        bottomSheetDialog = BottomSheetDialog(this)
        val behavior: BottomSheetBehavior<*> = bottomSheetDialog!!.behavior
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        val dialogView: View = View.inflate(this, R.layout.dialog_bottom_sheet, null)
        bottomSheetDialog?.setContentView(dialogView)
        val bottomDialogLlCollect =
            dialogView.findViewById<LinearLayout>(R.id.bottomDialogLlCollect)
        bottomDialogIvCollect = dialogView.findViewById(R.id.bottomDialogIvCollect)
        bottomDialogTvCollect = dialogView.findViewById(R.id.bottomDialogTvCollect)

        val bottomDialogLlCopy = dialogView.findViewById<LinearLayout>(R.id.bottomDialogLlCopy)
        val bottomDialogLlBrowser =
            dialogView.findViewById<LinearLayout>(R.id.bottomDialogLlBrowser)
        val bottomDialogLlShare = dialogView.findViewById<LinearLayout>(R.id.bottomDialogLlShare)
        val bottomDialogLlDynamic =
            dialogView.findViewById<LinearLayout>(R.id.bottomDialogLlDynamic)
        val bottomDialogLlReload = dialogView.findViewById<LinearLayout>(R.id.bottomDialogLlReload)
        val bottomDialogTvCancel = dialogView.findViewById<TextView>(R.id.bottomDialogTvCancel)
        if (isCollection == 1) {
            bottomDialogIvCollect.setImageResource(R.drawable.ic_collect_selected)
            bottomDialogTvCollect.text = getString(com.wzh.base.R.string.cancel_collection)
        } else {
            bottomDialogIvCollect.setImageResource(R.drawable.ic_collect)
            bottomDialogTvCollect.text = getString(com.wzh.base.R.string.collection)
        }
        bottomDialogLlCollect.setOnClickListener(this)
        bottomDialogLlCopy.setOnClickListener(this)
        bottomDialogLlBrowser.setOnClickListener(this)
        bottomDialogLlShare.setOnClickListener(this)
        bottomDialogLlDynamic.setOnClickListener(this)
        bottomDialogLlReload.setOnClickListener(this)
        bottomDialogTvCancel.setOnClickListener(this)
        bottomSheetDialog?.setContentView(dialogView) //给布局设置透明背景色
        ((dialogView.parent) as View).setBackgroundColor(Color.TRANSPARENT)
        bottomSheetDialog?.show()
    }

    companion object {
        fun actionStart(
            context: Context,
            article:Article
        ){
            val intent = Intent(context, ArticleActivity::class.java).apply{
                putExtra(PAGE_NAME,article.title)
                putExtra(PAGE_URL,article.link)
                putExtra(PAGE_ID,article.id)
                putExtra(IS_COLLECTION,if(article.collect) 1 else 0)
                putExtra(USER_ID,article.userId)
            }
            context.startActivity(intent)
        }
        fun actionStart(
            context: Context,
            pageName: String,
            pageUrl: String
        ) {
            val intent = Intent(context, ArticleActivity::class.java).apply {
                putExtra(PAGE_NAME, pageName)
                putExtra(PAGE_URL, pageUrl)
            }
            context.startActivity(intent)
        }
        fun actionStart(
            context:Context,
            collectX:CollectX
        ){
            val intent = Intent(context, ArticleActivity::class.java).apply {
                putExtra(PAGE_NAME, collectX.title)
                putExtra(PAGE_URL, collectX.link)
                putExtra(PAGE_ID, collectX.id)
                putExtra(IS_COLLECTION, 1)
                putExtra(ORIGIN_ID, collectX.originId)
                putExtra(USER_ID, collectX.userId)
            }
            context.startActivity(intent)
        }
    }
    override fun onResume() {
        super.onResume()
        binding.articleWebView.onResume()
    }

    override fun onPause() {
        super.onPause()
        binding.articleWebView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        bottomSheetDialog?.cancel()
        binding.articleWebView.destroy()
    }
}