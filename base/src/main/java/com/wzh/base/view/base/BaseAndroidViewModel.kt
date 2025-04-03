package com.wzh.base.view.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap

abstract class BaseAndroidViewModel<BaseData, Data, Key> : ViewModel() {
    val dataList = ArrayList<Data>()
    private val pageLiveData = MutableLiveData<Key>()

    fun getDataList(page: Key) {
        pageLiveData.value = page!!
    }

    val dataLiveData = pageLiveData.switchMap { page -> getData(page) }
    abstract fun getData(page: Key): LiveData<Result<BaseData>>

}