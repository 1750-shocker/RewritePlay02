package com.wzh.rewriteplay02.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.wzh.model.pojo.QueryHomeArticle
import com.wzh.model.room.entity.Article
import com.wzh.model.room.entity.BannerBean
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomePageViewModel @Inject constructor(
    private val homeRepository: HomeRepository
) : ViewModel() {

    private val pageLiveData = MutableLiveData<QueryHomeArticle>()

    val bannerList = ArrayList<BannerBean>()

    val bannerList2 = ArrayList<BannerBean>()

    val articleList = ArrayList<Article>()

    val articleLiveData = pageLiveData.switchMap { query ->
        homeRepository.getArticleList(query)
    }

    fun getBanner() = homeRepository.getBanner()

    fun getArticleList(page: Int, isRefresh: Boolean) {
        pageLiveData.value = QueryHomeArticle(page, isRefresh)
    }

}