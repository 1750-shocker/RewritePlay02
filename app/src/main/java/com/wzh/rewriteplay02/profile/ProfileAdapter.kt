package com.wzh.rewriteplay02.profile

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.wzh.base.Play
import com.wzh.base.view.base.BaseRecyclerAdapter
import com.wzh.rewriteplay02.databinding.AdapterProfileBinding
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

            mContext.getString(com.wzh.base.R.string.mine_blog) -> {
                //TODO:ArticleActivity.actionStart(mContext, mContext.getString(R.string.mine_blog),"pageUrl")
            }

            mContext.getString(com.wzh.base.R.string.mine_nuggets) -> {
                //TODO:ArticleActivity.actionStart(mContext, mContext.getString(R.string.mine_nuggets),"pageUrl")
            }

            mContext.getString(com.wzh.base.R.string.github) -> {
                //TODO:AboutActivity.actionStart(mContext,mContext.getString(R.string.mine_github), "pageUrl")
            }

            mContext.getString(com.wzh.base.R.string.about_me) -> {
                //TODO:UserActivity.actionStart(mContext)
            }
        }
    }

}

data class ProfileItem(var title: String, var imgId: Int)