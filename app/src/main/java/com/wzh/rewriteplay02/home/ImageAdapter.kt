package com.wzh.rewriteplay02.home

import android.content.Context
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wzh.base.util.checkNetworkAvailable
import com.wzh.base.util.showToast
import com.wzh.model.room.entity.BannerBean
import com.wzh.rewriteplay02.article.ArticleActivity
import com.youth.banner.adapter.BannerAdapter

open class ImageAdapter(private val mContext: Context, mData: List<BannerBean>) :
    BannerAdapter<BannerBean?, ImageAdapter.BannerViewHolder?>(mData) {

    class BannerViewHolder(view: ImageView) : RecyclerView.ViewHolder(view) {
        var imageView: ImageView = view
    }

    override fun onCreateHolder(parent: ViewGroup, viewType: Int): ImageAdapter.BannerViewHolder? {
        val imageView = ImageView(parent.context).apply {
            //必须设置为match_parent，这个是viewpager2强制要求的
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
            scaleType = ImageView.ScaleType.CENTER_CROP
        }
        return BannerViewHolder(imageView)
    }

    override fun onBindView(
        holder: BannerViewHolder?,
        data: BannerBean?,
        position: Int,
        size: Int
    ) {
        holder?.imageView?.apply {
            Glide.with(mContext)
                .load(if (data?.filePath == null) data?.imagePath else data?.filePath)
                .into(this)
            setOnClickListener {
                if (!mContext.checkNetworkAvailable()) {
                    mContext.showToast(mContext.getString(com.wzh.base.R.string.no_network))
                    return@setOnClickListener
                }
                ArticleActivity.actionStart(
                    mContext,
                    data?.title ?: "",
                    data?.url ?: "www.baidu.com"
                )
            }
        }
    }

}