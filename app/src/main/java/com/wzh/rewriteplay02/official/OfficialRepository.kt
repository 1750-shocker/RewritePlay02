package com.wzh.rewriteplay02.official

import android.app.Application
import com.wzh.base.util.DataStoreUtils
import com.wzh.model.pojo.QueryArticle
import com.wzh.model.room.AppDatabase
import com.wzh.model.room.entity.OFFICIAL
import com.wzh.network.base.AppNetwork
import com.wzh.rewriteplay02.base.liveDataFire
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@ActivityRetainedScoped
class OfficialRepository @Inject constructor(application: Application) {
    private val projectTabDao = AppDatabase.getDatabase(application).projectTabDao()
    private val articleDao = AppDatabase.getDatabase(application).articleDao()

    fun getWxArticleTree(isRefresh: Boolean) = liveDataFire {
        val projectTabList = projectTabDao.getAllOfficial()
        if (projectTabList.isNotEmpty() && !isRefresh) {
            Result.success(projectTabList)
        } else {
            val projectTree = AppNetwork.getWxArticleTree()
            if (projectTree.errorCode == 0) {
                val projectList = projectTree.data
                projectTabDao.insertList(projectList)
                Result.success(projectList)
            } else {
                Result.failure(RuntimeException("response status is ${projectTree.errorCode} msg is ${projectTree.errorMsg}"))
            }
        }
    }

    fun getWxArticle(query: QueryArticle) = liveDataFire {
        if (query.page == 1) {
            val dataStore = DataStoreUtils
            val articleListForChapterId = articleDao.getArticleListForCid(OFFICIAL, query.cid)
            var downArticleTime = 0L
            dataStore.readLongFlow(DOWN_OFFICIAL_ARTICLE_TIME, System.currentTimeMillis()).first {
                downArticleTime = it
                true
            }
            if (articleListForChapterId.isNotEmpty() && downArticleTime > 0 && downArticleTime - System.currentTimeMillis() < FOUR_HOUR && !query.isRefresh) {
                Result.success(articleListForChapterId)
            } else {
                val projectTree = AppNetwork.getWxArticle(query.page, query.cid)
                if (projectTree.errorCode == 0) {
                    if (articleListForChapterId.isNotEmpty() && articleListForChapterId[0].link == projectTree.data.datas[0].link && !query.isRefresh) {
                        Result.success(articleListForChapterId)
                    } else {
                        projectTree.data.datas.forEach {
                            it.localType = OFFICIAL
                        }
                        DataStoreUtils.saveLongData(
                            DOWN_OFFICIAL_ARTICLE_TIME,
                            System.currentTimeMillis()
                        )
                        if (query.isRefresh) {
                            articleDao.deleteAll(OFFICIAL, query.cid)
                        }
                        articleDao.insertList(projectTree.data.datas)
                        Result.success(projectTree.data.datas)
                    }
                } else {
                    Result.failure(RuntimeException("response status is ${projectTree.errorCode} msg is ${projectTree.errorMsg}"))
                }
            }
        } else {
            val projectTree = AppNetwork.getWxArticle(query.page, query.cid)
            if (projectTree.errorCode == 0) {
                Result.success(projectTree.data.datas)
            } else {
                Result.failure(RuntimeException("response status is ${projectTree.errorCode} msg is ${projectTree.errorMsg}"))
            }
        }
    }
}

const val ONE_DAY = 1000 * 60 * 60 * 24
const val FOUR_HOUR = 1000 * 60 * 60 * 4
const val DOWN_IMAGE_TIME = "DownImageTime"
const val DOWN_TOP_ARTICLE_TIME = "DownTopArticleTime"
const val DOWN_ARTICLE_TIME = "DownArticleTime"
const val DOWN_PROJECT_ARTICLE_TIME = "DownProjectArticleTime"
const val DOWN_OFFICIAL_ARTICLE_TIME = "DownOfficialArticleTime"