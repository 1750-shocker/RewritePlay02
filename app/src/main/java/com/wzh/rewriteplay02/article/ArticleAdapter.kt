package com.wzh.rewriteplay02.article

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.wzh.base.Play
import com.wzh.base.util.checkNetworkAvailable
import com.wzh.base.util.getHtmlText
import com.wzh.base.util.setSafeListener
import com.wzh.base.util.showToast
import com.wzh.base.view.base.BaseRecyclerAdapter
import com.wzh.model.room.AppDatabase
import com.wzh.model.room.entity.Article
import com.wzh.model.room.entity.HISTORY
import com.wzh.rewriteplay02.R
import com.wzh.rewriteplay02.article.collect.CollectRepository
import com.wzh.rewriteplay02.article.collect.CollectRepositoryEntryPoint
import com.wzh.rewriteplay02.databinding.ItemArticleBinding
import dagger.hilt.android.EntryPointAccessors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 从外部获取数据集合
 */

class ArticleAdapter(
    private val mContext: Context,
    private val articleList: ArrayList<Article>,
    private val isShowCollect: Boolean = true,
) : BaseRecyclerAdapter<ItemArticleBinding>(), CoroutineScope by MainScope() {

    /**
     * 在父类中定义ViewHolder基类，指定泛型为ItemArticleBinding也是ViewBinding的子类
     * 代表过会传参的是ItemArticleBinding类型
     */
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerHolder<ItemArticleBinding> {
        //从布局文件inflate并构建ViewHolder
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BaseRecyclerHolder(binding)
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    //绑定数据，数据都是外部传入，这里只负责绑定，然后判断一下数据库里有没有，没有就插入
    //也就是说这里的适配器会负责一部分数据更新操作
    override fun onBaseBindViewHolder(binding: ItemArticleBinding, position: Int) {
        val data = articleList[position]
        val collectRepository =
            EntryPointAccessors.fromApplication(mContext, CollectRepositoryEntryPoint::class.java)
                .getCollectRepository()
        binding.apply {
            if (!TextUtils.isEmpty(data.title))
                tvArticleTitle.text = getHtmlText(data.title)
            tvArticleChapter.text = data.superChapterName
            tvArticleAuthor.text =
                if (TextUtils.isEmpty(data.author)) data.shareUser else data.author
            tvArticleTime.text = data.niceShareDate
            if (!TextUtils.isEmpty(data.envelopePic)) {
                ivArticleImage.visibility = VISIBLE
                Glide.with(mContext).load(data.envelopePic).into(ivArticleImage)
            } else {
                ivArticleImage.visibility = GONE
            }
            ivArticleCollect.isVisible = isShowCollect
            if (data.collect) {
                ivArticleCollect.setImageResource(R.drawable.ic_favorite_black_24dp)
            } else {
                ivArticleCollect.setImageResource(R.drawable.ic_favorite_border_black_24dp)
            }
            launch {
                Play.isLogin().collectLatest {
                    ivArticleCollect.isVisible = it
                }
            }
            ivArticleCollect.setSafeListener {
                launch {
                    Play.isLogin().collectLatest {
                        if(it){
                            if(mContext.checkNetworkAvailable()){
                                //WHY:这里有逻辑冲突，自动先亮一下再灭一下，弹出toast
                               data.collect = !data.collect
                               setCollect(collectRepository, data, ivArticleCollect)
                            }else{
                                mContext.showToast(mContext.getString(com.wzh.base.R.string.no_network))
                            }
                        }else{
                            mContext.showToast(mContext.getString(com.wzh.base.R.string.not_currently_logged_in))
                        }
                    }
                }
            }
            articleItem.setOnClickListener {
                if (!mContext.checkNetworkAvailable()) {
                    mContext.showToast(mContext.getString(com.wzh.base.R.string.no_network))
                    return@setOnClickListener
                }
                ArticleActivity.actionStart(mContext, data)
                val articleDao = AppDatabase.getDatabase(mContext).articleDao()
                launch(Dispatchers.IO) {
                    if (articleDao.getArticle(data.id, HISTORY) == null) {
                        data.localType = HISTORY
                        data.desc = ""
                        articleDao.insert(data)
                    }
                }
            }
        }

    }

    private fun setCollect(
        collectRepository: CollectRepository,
        article: Article,
        articleIvCollect: ImageView
    ) {
        launch(Dispatchers.IO) {
            val articleDao = AppDatabase.getDatabase(mContext).articleDao()
            if (!article.collect) {
                val cancelCollects = collectRepository.cancelCollects(article.id)
                if (cancelCollects.errorCode == 0) {
                    withContext(Dispatchers.Main) {
                        articleIvCollect.setImageResource(R.drawable.ic_favorite_border_black_24dp)
                        Log.d("wzhhhh", "setCollect: 不知道为什么执行到这里")
                        mContext.showToast(mContext.getString(com.wzh.base.R.string.collection_cancelled_successfully))
                        articleDao.update(article)
                    }
                } else {
                    mContext.showToast(mContext.getString(com.wzh.base.R.string.failed_to_cancel_collection))
                }
            } else {
                val toCollects = collectRepository.toCollects(article.id)
                if (toCollects.errorCode == 0) {
                    withContext(Dispatchers.Main) {
                        articleIvCollect.setImageResource(R.drawable.ic_favorite_black_24dp)
                        mContext.showToast(mContext.getString(com.wzh.base.R.string.collection_successful))
                        articleDao.update(article)
                    }
                } else {
                    mContext.showToast(mContext.getString(com.wzh.base.R.string.collection_failed))
                }
            }
        }
    }
}