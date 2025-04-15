package com.wzh.rewriteplay02.profile.share

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.wzh.model.model.ShareArticles
import com.wzh.model.room.entity.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ShareViewModel @Inject constructor(private val shareRepository: ShareRepository) :
    ViewModel() {
    val articleList = ArrayList<Article>()
    private val pageLiveData = MutableLiveData<Int>()
    private val pageAndCidLiveData = MutableLiveData<ShareArticle>()
    val articleLiveData = pageLiveData.switchMap { page ->
        shareRepository.getMyShareList(page)
    }
    val articleAndCidLiveData = pageAndCidLiveData.switchMap { page ->
        shareRepository.getShareList(page.cid, page.page)
    }

    fun getArticleList(page: Int) {
        pageLiveData.value = page
    }

    fun getArticleList(cid: Int, page: Int) {
        pageAndCidLiveData.value = ShareArticle(cid, page)
    }
}

data class ShareArticle(val page: Int, val cid: Int)