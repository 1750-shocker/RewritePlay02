package com.wzh.rewriteplay02.home.search

import android.app.Application
import android.util.Log
import com.wzh.model.room.AppDatabase
import com.wzh.network.base.AppNetwork
import com.wzh.rewriteplay02.base.liveDataFire
import com.wzh.rewriteplay02.base.liveDataModel
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject


@ActivityRetainedScoped
class SearchRepository @Inject constructor(application: Application) {

    private val hotKeyDao = AppDatabase.getDatabase(application).hotKeyDao()

    /**
     * 获取搜索热词
     */
    suspend fun getHotKeyList() = hotKeyDao.getHotKeyList()
    fun getHotKey() = liveDataFire {
        val hotKeyList = hotKeyDao.getHotKeyList()
        for (hot in hotKeyList) {
            Log.d("wzhhhh", "数据库中的数据:${hot.name}")
        }
        if (hotKeyList.isNotEmpty()) {
            Result.success(hotKeyList)
        } else {
            val projectTree = AppNetwork.getHotKey()
            if (projectTree.errorCode == 0) {
                val hotKeyLists = projectTree.data
                hotKeyDao.insertList(hotKeyLists)
                Result.success(hotKeyLists)
            } else {
                Result.failure(RuntimeException("response status is ${projectTree.errorCode}  msg is ${projectTree.errorMsg}"))
            }
        }
    }

    /**
     * 获取搜索结果
     */
    fun getQueryArticleList(page: Int, k: String) = liveDataModel {
        AppNetwork.getQueryArticleList(page, k)
    }

}