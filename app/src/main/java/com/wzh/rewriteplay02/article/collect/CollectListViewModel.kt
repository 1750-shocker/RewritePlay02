package com.wzh.rewriteplay02.article.collect

import androidx.lifecycle.LiveData
import com.wzh.base.view.base.BaseViewModel
import com.wzh.model.model.Collect
import com.wzh.model.model.CollectX
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CollectListViewModel @Inject constructor(private val collectRepository: CollectRepository) :
    BaseViewModel<Collect, CollectX, Int>() {

    override fun getData(key: Int): LiveData<Result<Collect>> {
        return collectRepository.getCollectList(key - 1)
    }

}