package com.wzh.rewriteplay02.article

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.wzh.base.util.getHtmlText
import com.wzh.base.view.base.BaseRecyclerAdapter
import com.wzh.model.room.AppDatabase
import com.wzh.model.room.entity.Article
import com.wzh.model.room.entity.HISTORY
import com.wzh.rewriteplay02.R
import com.wzh.rewriteplay02.databinding.ItemArticleBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.launch

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
        Log.e("wocaonima", "走进Adapter了，在填充视图", )
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
        //TODO:收藏Repository初始化
        binding.apply {
            if (!TextUtils.isEmpty(data.title)) {
                tvArticleTitle.text = getHtmlText(data.title)
            }
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
            //TODO:这里忽略两个视图
            ivArticleCollect.isVisible = isShowCollect
            if (data.collect) {
                ivArticleCollect.setImageResource(R.drawable.ic_favorite_black_24dp)
            } else {
                ivArticleCollect.setImageResource(R.drawable.ic_favorite_border_black_24dp)
            }
            //TODO：加一个launch，读取登陆状态，由此判断显示收藏图标与否，设置收藏按钮的特殊监听器
            articleItem.setOnClickListener {
                //TODO:通过扩展函数判断网络状态不行就跳弹窗
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

}