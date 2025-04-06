package com.wzh.base.view.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap

abstract class BaseViewModel<BaseData, Data, Key> : ViewModel() {
    val dataList = ArrayList<Data>()
    private val pageLiveData = MutableLiveData<Key>()

    fun getDataList(key: Key) {
        pageLiveData.value = key!!
    }

    val dataLiveData = pageLiveData.switchMap { key -> getData(key) }

    abstract fun getData(key: Key): LiveData<Result<BaseData>>

}