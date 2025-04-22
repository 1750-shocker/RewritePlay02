package com.wzh.rewriteplay02.profile.history

import android.app.Application
import com.wzh.model.room.AppDatabase
import com.wzh.model.room.entity.HISTORY
import com.wzh.rewriteplay02.base.liveDataFire
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class BrowseHistoryRepository @Inject constructor(val application: Application) {

    private val browseHistoryDao = AppDatabase.getDatabase(application).articleDao()

    /**
     * 获取历史记录列表
     */
    fun getBrowseHistory(page: Int) = liveDataFire {
        val projectClassifyLists = browseHistoryDao.getHistoryArticleList((page - 1) * 20,HISTORY)
        if (projectClassifyLists.isNotEmpty()) {
            Result.success(projectClassifyLists)
        } else {
            Result.failure(RuntimeException("response status is "))
        }

    }

}