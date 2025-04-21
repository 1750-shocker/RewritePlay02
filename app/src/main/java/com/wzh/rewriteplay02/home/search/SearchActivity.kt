package com.wzh.rewriteplay02.home.search

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.wzh.base.util.hideIme
import com.wzh.base.util.showIme
import com.wzh.base.util.showToast
import com.wzh.base.view.base.BaseActivity
import com.wzh.model.room.AppDatabase
import com.wzh.model.room.dao.HotKeyDao
import com.wzh.model.room.entity.HotKey
import com.wzh.rewriteplay02.R
import com.wzh.rewriteplay02.databinding.ActivitySearchBinding
import com.wzh.rewriteplay02.home.search.article.ArticleListActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class SearchActivity : BaseActivity(), View.OnClickListener, TextView.OnEditorActionListener {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var hotKeyDao: HotKeyDao
    private val viewModel by viewModels<SearchViewModel>()

    override fun getRealLayoutView(): View {
        binding = ActivitySearchBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        addFlowView()
        showIme(binding.searchTxtKeyword)
    }

    override fun initData() {
        viewModel.getDataList(true)
        hotKeyDao = AppDatabase.getDatabase(this).hotKeyDao()
        setDataStatus(viewModel.dataLiveData) {
            if (it.isNotEmpty() && viewModel.dataList.isEmpty()) {
                viewModel.dataList.clear()
                viewModel.dataList.addAll(it)
            }
            addFlowView()
        }
    }

    override fun isSearchPage(): Boolean {
        return true
    }

    override fun onPause() {
        super.onPause()
        hideIme(binding.searchTxtKeyword)
    }

    private fun addFlowView() {
        //往容器内添加TextView数据
        val layoutParams = LinearLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        layoutParams.setMargins(10, 5, 10, 15)
        binding.searchFlowLayout.removeAllViews()
        for (i in 0 until viewModel.dataList.size) {
            val item = View.inflate(this, R.layout.tag_item, null)
            val tv = item.findViewById<TextView>(R.id.searchTvName)
            val delete = item.findViewById<LinearLayout>(R.id.searchLlDelete)
            val name = viewModel.dataList[i].name
            if (viewModel.dataList[i].order > 0) {
                delete.visibility = View.GONE
            }
            tv.text = name
            tv.setOnClickListener {
                ArticleListActivity.actionStart(this, name)
            }
            delete.setOnClickListener {
                //WHY:这里缺少viewModel.dataList()的更新，但是这里的索引i和item绑定了，会导致更新错乱
                lifecycleScope.launch {
                    //WHY:这里的数据库操作是延迟异步操作，如果快速重进activity，会发现数据未更新
                    hotKeyDao.delete(viewModel.dataList[i])
                }
                binding.searchFlowLayout.removeView(item)
            }
            binding.searchFlowLayout.addView(item, layoutParams)
        }
    }


    override fun initView() {
        binding.apply {
            searchImgBack.setOnClickListener(this@SearchActivity)
            searchTxtRight.setOnClickListener(this@SearchActivity)
            searchTxtKeyword.setOnEditorActionListener(this@SearchActivity)
        }
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.searchImgBack -> {
                finish()
                overridePendingTransition(0, R.anim.search_push_out)
            }
            R.id.searchTxtRight -> {
                toSearch()
            }
        }
    }

    private fun toSearch() {
        val keyword = binding.searchTxtKeyword.text.toString()
        if (TextUtils.isEmpty(keyword)) {
            showToast(getString(com.wzh.base.R.string.keyword_not_null))
            return
        }
        val hotKey = HotKey(id = -1, name = keyword)
        lifecycleScope.launch {
            hotKeyDao.insert(hotKey)
        }
        viewModel.dataList.add(0, hotKey)
        ArticleListActivity.actionStart(this, keyword)
    }

    companion object {
        fun actionStart(context: Context) {
            val intent = Intent(context, SearchActivity::class.java)
            context.startActivity(intent)
        }
    }

    override fun onEditorAction(v: TextView?, actionId: Int, event: KeyEvent?): Boolean {
        //以下方法防止两次发送请求
        if (event == null) return false
        return if (actionId == EditorInfo.IME_ACTION_SEND || event.keyCode == KeyEvent.KEYCODE_ENTER) {
            when (event.action) {
                KeyEvent.ACTION_UP -> {
                    //发送请求
                    toSearch()
                    true
                }
                else -> true
            }
        } else false
    }

}
