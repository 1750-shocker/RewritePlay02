package com.wzh.rewriteplay02.article

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import com.wzh.base.view.base.BaseRecyclerAdapter
import com.wzh.model.room.entity.Article
import com.wzh.rewriteplay02.databinding.ItemArticleBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope

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
        val binding = ItemArticleBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return BaseRecyclerHolder(binding)
    }

    override fun getItemCount(): Int {
        return articleList.size
    }

    //绑定数据
    override fun onBaseBindViewHolder(binding: ItemArticleBinding, position: Int) {
        val data = articleList[position]
        //TODO:通过EntryPoint从hilt获取repository
        binding.apply {
            //TODO:从data中设置数据到控件
            //TODO:给收藏按钮设置点击事件
            //TODO：给item项设置点击事件
        }
    }

}