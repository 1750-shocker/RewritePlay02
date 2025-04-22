package com.wzh.rewriteplay02.profile.history

import androidx.lifecycle.LiveData
import com.wzh.base.view.base.BaseViewModel
import com.wzh.model.room.entity.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class BrowseHistoryViewModel @Inject constructor(
    private val browseHistoryRepository: BrowseHistoryRepository
) : BaseViewModel<List<Article>, Article, Int>() {

    override fun getData(key: Int): LiveData<Result<List<Article>>> {
        return browseHistoryRepository.getBrowseHistory(key)
    }

}