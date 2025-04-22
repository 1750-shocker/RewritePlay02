package com.wzh.rewriteplay02.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.wzh.base.Play
import com.wzh.base.view.base.BaseRecyclerAdapter
import com.wzh.rewriteplay02.article.ArticleActivity
import com.wzh.rewriteplay02.article.collect.CollectListActivity
import com.wzh.rewriteplay02.databinding.AdapterProfileBinding
import com.wzh.rewriteplay02.profile.history.BrowseHistoryActivity
import com.wzh.rewriteplay02.profile.login.LoginActivity

class ProfileAdapter(
    private val mContext: Context,
    private val profileItemList: ArrayList<ProfileItem>
) : BaseRecyclerAdapter<AdapterProfileBinding>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BaseRecyclerHolder<AdapterProfileBinding> {
        val binding =
            AdapterProfileBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return BaseRecyclerHolder(binding)
    }

    override fun getItemCount(): Int {
        return profileItemList.size
    }

    override fun onBaseBindViewHolder(binding: AdapterProfileBinding, position: Int) {
        val data = profileItemList[position]
        binding.apply {
            profileAdTitle.text = data.title
            profileAdIv.setImageResource(data.imgId)
            profileItem.setOnClickListener {
                toJump(data.title)
            }
        }
    }

    private fun toJump(title: String) {
        when (title) {
            mContext.getString(com.wzh.base.R.string.mine_points) -> {
                if (Play.isLoginResult()) {
                    //TODO:UserRankActivity.actionStart(mContext)
                } else {
                    Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show()
                }
            }

            mContext.getString(com.wzh.base.R.string.my_collection) -> {
                if (Play.isLoginResult()) {
                    CollectListActivity.actionStart(mContext)
                } else {
                    LoginActivity.actionStart(mContext)
                }
            }

            mContext.getString(com.wzh.base.R.string.mine_blog) -> {
                ArticleActivity.actionStart(
                    mContext, mContext.getString(com.wzh.base.R.string.mine_blog),
                    "https://blog.csdn.net/qq_45205637?type=blog"
                )
            }

            mContext.getString(com.wzh.base.R.string.browsing_history) -> {
                BrowseHistoryActivity.actionStart(mContext)
            }

            mContext.getString(com.wzh.base.R.string.mine_nuggets) -> {
                ArticleActivity.actionStart(
                    mContext,
                    mContext.getString(com.wzh.base.R.string.mine_nuggets),
                    "https://juejin.cn/user/3153816165032333"
                )
            }

            mContext.getString(com.wzh.base.R.string.github) -> {
                ArticleActivity.actionStart(mContext,mContext.getString(com.wzh.base.R.string.mine_github), "https://github.com/1750-shocker")
            }

            mContext.getString(com.wzh.base.R.string.about_me) -> {
                //TODO:UserActivity.actionStart(mContext)
            }
        }
    }

}

data class ProfileItem(var title: String, var imgId: Int)