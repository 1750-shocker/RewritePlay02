package com.wzh.rewriteplay02.project

import android.app.Application
import com.wzh.base.util.DataStoreUtils
import com.wzh.model.pojo.QueryArticle
import com.wzh.model.room.AppDatabase
import com.wzh.model.room.entity.PROJECT
import com.wzh.network.base.AppNetwork
import com.wzh.rewriteplay02.base.liveDataFire
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.first
import javax.inject.Inject

@ActivityRetainedScoped
class ProjectRepository @Inject constructor(private val application: Application) {
    private val projectTabDao = AppDatabase.getDatabase(application).projectTabDao()
    private val articleDao = AppDatabase.getDatabase(application).articleDao()


    fun getProjectTab(isRefresh: Boolean) = liveDataFire {
        val projectTabList = projectTabDao.getProjectTabList()
        if (projectTabList.isNotEmpty() && !isRefresh) {
            Result.success(projectTabList)
        } else {
            val baseModel = AppNetwork.getProjectTab()
            if (baseModel.errorCode == 0) {
                val projectTabList1 = baseModel.data
                projectTabDao.insertList(projectTabList1)
                Result.success(projectTabList1)
            } else {
                Result.failure(
                    (RuntimeException(
                        "response status is ${baseModel.errorCode}" +
                                "msg is ${baseModel.errorMsg}"
                    ))
                )
            }
        }
    }

    fun getProjectList(query: QueryArticle) = liveDataFire {
        if (query.page == 1) {//第一页，为了快速加载，先从数据库拿数据，拿不到再去网络拿

            val articleList =
                articleDao.getArticleListForCid(PROJECT, query.cid)
            var downArticleTime = 0L
            //从dataStore拿当时缓存的时间戳
            val dataStore = DataStoreUtils
            dataStore.readLongFlow(DOWN_PROJECT_ARTICLE_TIME, System.currentTimeMillis()).first {
                downArticleTime = it
                true
            }
            //判断过期
            if (articleList.isNotEmpty() && downArticleTime > 0 && downArticleTime - System.currentTimeMillis() < FOUR_HOUR && !query.isRefresh) {
                //没过期
                Result.success(articleList)
            } else {
                //过期了
                val baseModel = AppNetwork.getProjectList(1, query.cid)
                if (baseModel.errorCode == 0) {
                    //过期的数据和最新的数据对比，如果相同就不更新数据库
                    if (articleList.isNotEmpty() && articleList[0].link == baseModel.data.datas[0].link && !query.isRefresh) {
                        Result.success(articleList)
                    } else {
                        //更新数据库
                        baseModel.data.datas.forEach {
                            it.localType = PROJECT
                        }
                        dataStore.saveLongData(
                            DOWN_PROJECT_ARTICLE_TIME,
                            System.currentTimeMillis()
                        )
                        if (query.isRefresh) {
                            articleDao.deleteAll(PROJECT, query.cid)
                        }
                        articleDao.insertList(baseModel.data.datas)
                        Result.success(baseModel.data.datas)
                    }
                } else {
                    Result.failure(RuntimeException("response status is ${baseModel.errorCode}  msg is ${baseModel.errorMsg}"))
                }
            }
        } else {
            val baseModel = AppNetwork.getProjectList(query.page, query.cid)
            if (baseModel.errorCode == 0) {
                Result.success(baseModel.data.datas)
            } else {
                Result.failure(RuntimeException("response status is ${baseModel.errorCode}msg is ${baseModel.errorMsg}"))
            }
        }
    }
}

const val DOWN_PROJECT_ARTICLE_TIME = "DownProjectArticleTime"
const val FOUR_HOUR = 1000 * 60 * 60 * 4