package com.wzh.rewriteplay02.home.search.article

import androidx.lifecycle.LiveData
import com.wzh.base.view.base.BaseViewModel
import com.wzh.model.model.ArticleList
import com.wzh.model.room.entity.Article
import com.wzh.rewriteplay02.home.search.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class ArticleListViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : BaseViewModel<ArticleList, Article, QueryKeyArticle>() {

    override fun getData(key: QueryKeyArticle): LiveData<Result<ArticleList>> {
        return searchRepository.getQueryArticleList(key.page, key.k)
    }

}

data class QueryKeyArticle(var page: Int, var k: String)