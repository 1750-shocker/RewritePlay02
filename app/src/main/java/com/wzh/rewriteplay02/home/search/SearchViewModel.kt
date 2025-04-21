package com.wzh.rewriteplay02.home.search

import androidx.lifecycle.LiveData
import com.wzh.base.view.base.BaseViewModel
import com.wzh.model.room.entity.HotKey
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository
) : BaseViewModel<List<HotKey>, HotKey, Boolean>() {

    override fun getData(key: Boolean): LiveData<Result<List<HotKey>>> {
        return searchRepository.getHotKey()
    }
    suspend fun getHotKeyList() = searchRepository.getHotKeyList()

}