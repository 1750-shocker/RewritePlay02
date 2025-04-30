package com.wzh.rewriteplay02.profile.collect

import android.content.Context
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleCoroutineScope
import com.bumptech.glide.Glide
import com.wzh.base.util.getHtmlText
import com.wzh.base.util.setSafeListener
import com.wzh.base.util.showToast
import com.wzh.base.view.base.BaseRecyclerAdapter
import com.wzh.model.model.CollectX
import com.wzh.rewriteplay02.R
import com.wzh.rewriteplay02.article.ArticleActivity
import com.wzh.rewriteplay02.databinding.ItemArticleBinding
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import com.wzh.base.util.checkNetworkAvailable


class CollectAdapter(
    private val mContext: Context,
    private val articleList: ArrayList<CollectX>,
    private val lifecycleScope: LifecycleCoroutineScope,
) : BaseRecyclerAdapter<ItemArticleBinding>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerHolder<ItemArticleBinding> {
        val binding =
            ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BaseRecyclerHolder(binding)
    }

    private fun cancelCollect(id: Int, position: Int) {
        lifecycleScope.launch {
            val collectRepository =
                EntryPointAccessors.fromApplication(
                    mContext,
                    CollectRepositoryEntryPoint::class.java
                ).getCollectRepository()
            val cancelCollects = collectRepository.cancelCollects(id)
            withContext(Dispatchers.Main) {
                if (cancelCollects.errorCode == 0) {
                    mContext.showToast(mContext.getString(com.wzh.base.R.string.collection_cancelled_successfully))
                    articleList.removeAt(position)
                    notifyItemChanged(position)
                } else {
                    mContext.showToast(mContext.getString(com.wzh.base.R.string.failed_to_cancel_collection))
                }
            }
        }
    }

    override fun onBaseBindViewHolder(binding: ItemArticleBinding, position: Int) {
        val data = articleList[position]
        binding.apply {
            tvArticleTitle.text = getHtmlText(data.title)
            tvArticleChapter.text = data.chapterName
            tvArticleAuthor.text =
                if (TextUtils.isEmpty(data.author)) data.chapterName else data.author
            tvArticleTime.text = data.niceDate

            if (!TextUtils.isEmpty(data.envelopePic)) {
                ivArticleImage.visibility = View.VISIBLE
                Glide.with(mContext).load(data.envelopePic).into(ivArticleImage)
            } else {
                ivArticleImage.visibility = View.GONE
            }

            ivArticleCollect.setImageResource(R.drawable.ic_favorite_black_24dp)
            ivArticleCollect.setSafeListener {
                cancelCollect(data.originId, position)
            }
            articleItem.setOnClickListener {
                if (!mContext.checkNetworkAvailable()) {
                    mContext.showToast(mContext.getString(com.wzh.base.R.string.no_network))
                    return@setOnClickListener
                }
                ArticleActivity.actionStart(mContext, data)
            }
        }
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

}