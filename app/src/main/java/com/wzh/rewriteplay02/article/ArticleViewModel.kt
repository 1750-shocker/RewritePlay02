package com.wzh.rewriteplay02.article

import android.view.View.OnClickListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.wzh.base.view.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ArticleViewModel @Inject constructor(private val articleRepository: ArticleRepository) :
    ViewModel() {
    fun setCollect(
        isCollect: Int,
        pageId: Int,
        originId: Int,
        collectListener: (Boolean) -> Unit
    ) {
        viewModelScope.launch {
            articleRepository.setCollect(isCollect, pageId, originId, collectListener)
        }
    }
}