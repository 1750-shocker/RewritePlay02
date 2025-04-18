package com.wzh.rewriteplay02.profile

import android.app.AlertDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.wzh.base.Play
import com.wzh.base.Play.logout
import com.wzh.rewriteplay02.R
import com.wzh.rewriteplay02.article.ArticleBroadCast
import com.wzh.rewriteplay02.base.ArticleCollectBaseFragment
import com.wzh.rewriteplay02.databinding.FragmentProfileBinding
import com.wzh.rewriteplay02.profile.login.AccountRepository
import com.wzh.rewriteplay02.profile.login.LoginActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : ArticleCollectBaseFragment(), View.OnClickListener {

    private var binding: FragmentProfileBinding? = null

    @Inject
    lateinit var accountRepository: AccountRepository

    private lateinit var profileAdapter: ProfileAdapter

    private lateinit var nameArray: Array<String>
    private var profileItemList = ArrayList<ProfileItem>()
    private var dialog: AlertDialog? = null
    private val imageArray = arrayOf(
        R.drawable.ic_integral,
        R.drawable.ic_profile_collect,
        R.drawable.ic_csdn,
        R.drawable.ic_history,
        R.drawable.ic_cnblogs,
        R.drawable.ic_github,
        R.drawable.ic_profile_mine
    )

    override fun refreshData() {
        lifecycleScope.launch {
            Play.isLogin().collectLatest {
                binding?.apply {
                    if (it) {
                        profileIvHead.setImageResource(R.drawable.ic_head)
                        profileTvName.text = Play.nickName
                        profileTvRank.text = Play.username
                        profileBtnLogout.visibility = View.VISIBLE
                    } else {
                        clearInfo()
                    }
                }
            }
        }
    }

    private fun clearInfo() {
        binding?.apply {
            profileBtnLogout.visibility = View.GONE
            profileIvHead.setImageResource(R.drawable.img_normal_head)
            profileTvName.text = getString(com.wzh.base.R.string.no_login)
            profileTvRank.text = getString(com.wzh.base.R.string.click_login)
        }
    }

    override fun getRealLayoutView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        attachToRoot: Boolean
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun initData() {
        if (profileItemList.size == 0) {
            nameArray = arrayOf(
                getString(com.wzh.base.R.string.mine_points),
                getString(com.wzh.base.R.string.my_collection),
                getString(com.wzh.base.R.string.mine_blog),
                getString(com.wzh.base.R.string.browsing_history),
                getString(com.wzh.base.R.string.mine_nuggets),
                getString(com.wzh.base.R.string.github),
                getString(com.wzh.base.R.string.about_me)
            )
            for (index in nameArray.indices) {
                profileItemList.add(
                    ProfileItem(nameArray[index], imageArray[index])
                )
            }
            profileAdapter.notifyItemInserted(profileItemList.size)
        }
    }

    override fun initView() {
        binding?.apply {
            profileTitleBar.setRightImage(R.drawable.btn_right_right_bg)
            profileTitleBar.setRightImgOnClickListener {
                //TODO:RankActivity.actionStart(requireContext)
            }
            profileIvHead.setOnClickListener(this@ProfileFragment)
            profileTvName.setOnClickListener(this@ProfileFragment)
            profileTvRank.setOnClickListener(this@ProfileFragment)
            profileRv.layoutManager = LinearLayoutManager(context)
            profileAdapter = ProfileAdapter(requireContext(), profileItemList)
            profileRv.adapter = profileAdapter
            refreshData()
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.profileIvHead, R.id.profileTvName, R.id.profileTvRank -> personalInformation()
            R.id.profileBtnLogout -> setLogout()
            R.id.logout_cancel -> dialog?.dismiss()
            R.id.logout_sure -> {
                dialog?.dismiss()
                clearInfo()
                logout()
                ArticleBroadCast.sendArticleChangeReceiver(requireContext())
                accountRepository.getLogout()
            }
        }
    }

    private fun setLogout() {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_logout, null)
        view.apply {
            findViewById<TextView>(R.id.logout_cancel).setOnClickListener(this@ProfileFragment)
            findViewById<TextView>(R.id.logout_sure).setOnClickListener(this@ProfileFragment)
        }
        dialog = AlertDialog.Builder(context).setView(view).create()
        //WHY:这里这个背景试一下不设置是什么效果
        dialog?.window?.setBackgroundDrawableResource(android.R.color.transparent)
        dialog?.show()
    }

    private fun personalInformation() {
        if (!Play.isLoginResult()) {
           LoginActivity.actionStart(requireContext())
        } else {
            //TODO:LShareActivity.actionStart(requireContext(), true)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ProfileFragment()
    }
}