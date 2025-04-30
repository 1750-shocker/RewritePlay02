package com.wzh.rewriteplay02.home

import android.annotation.SuppressLint
import android.app.Application
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.wzh.base.util.DataStoreUtils
import com.wzh.model.pojo.QueryHomeArticle
import com.wzh.model.room.AppDatabase
import com.wzh.model.room.dao.BannerBeanDao
import com.wzh.model.room.entity.Article
import com.wzh.model.room.entity.BannerBean
import com.wzh.model.room.entity.HOME
import com.wzh.model.room.entity.HOME_TOP
import com.wzh.network.base.AppNetwork
import com.wzh.rewriteplay02.base.liveDataFire
import com.wzh.rewriteplay02.official.DOWN_ARTICLE_TIME
import com.wzh.rewriteplay02.official.DOWN_IMAGE_TIME
import com.wzh.rewriteplay02.official.DOWN_TOP_ARTICLE_TIME
import com.wzh.rewriteplay02.official.FOUR_HOUR
import com.wzh.rewriteplay02.official.ONE_DAY
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@ActivityRetainedScoped
class HomeRepository @Inject constructor(val application: Application) {

    companion object {
        private const val TAG = "HomeRepository"
    }

    /**
     * 获取banner
     */
    fun getBanner() = liveDataFire {
        val dataStore = DataStoreUtils
        var downImageTime = 0L
        dataStore.readLongFlow(DOWN_IMAGE_TIME, System.currentTimeMillis()).first {
            downImageTime = it
            true
        }
        val bannerBeanDao = AppDatabase.getDatabase(application).bannerBeanDao()
        val bannerBeanList = bannerBeanDao.getBannerBeanList()
        if (bannerBeanList.isNotEmpty() && downImageTime > 0 && downImageTime - System.currentTimeMillis() < ONE_DAY) {
            Result.success(bannerBeanList)
        } else {
            val bannerResponse = AppNetwork.getBanner()
            if (bannerResponse.errorCode == 0) {
                val bannerList = bannerResponse.data
                dataStore.saveLongData(DOWN_IMAGE_TIME, System.currentTimeMillis())
                if (bannerBeanList.isNotEmpty() && bannerBeanList[0].url == bannerList[0].url) {
                    Result.success(bannerBeanList)
                } else {
                    bannerBeanDao.deleteAll()
                    insertBannerList(bannerBeanDao, bannerList)
                    Result.success(bannerList)
                }
            } else {
                Result.failure(RuntimeException("response status is ${bannerResponse.errorCode}  msg is ${bannerResponse.errorMsg}"))
            }
        }
    }

    @SuppressLint("CheckResult")
    private fun insertBannerList(
        bannerBeanDao: BannerBeanDao,
        bannerList: List<BannerBean>
    ) {
        val uiScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        bannerList.forEach {
            val mRequestManager: RequestManager = Glide.with(application)
            val mRequestBuilder: RequestBuilder<File> = mRequestManager.downloadOnly()
            mRequestBuilder.load(it.imagePath)
            mRequestBuilder.listener(object : RequestListener<File> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: Target<File>,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e(TAG, "insertBannerList onLoadFailed: ${e?.message}")
                    return false
                }

                override fun onResourceReady(
                    resource: File,
                    model: Any,
                    target: Target<File>?,
                    dataSource: DataSource,
                    isFirstResource: Boolean
                ): Boolean {
                    try {
                        it.filePath = resource.absolutePath ?: ""
                        uiScope.launch {
                            if (it.filePath.isNotEmpty()) {
                                bannerBeanDao.insert(it)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Log.e(TAG, "insertBannerList onResourceReady: ${e.message}")
                    }
                    return false
                }
            })
            mRequestBuilder.preload()
        }
    }

    /**
     * 首页获取文章列表
     * @param query 查询条件
     */
    fun getArticleList(query: QueryHomeArticle) = liveDataFire {
        coroutineScope {
            val res = arrayListOf<Article>()
            if (query.page == 1) {
                val dataStore = DataStoreUtils
                var downArticleTime = 0L
                //读取时间戳保存到downArticleTime
                dataStore.readLongFlow(DOWN_ARTICLE_TIME, System.currentTimeMillis()).first {
                    downArticleTime = it
                    true
                }
                val articleListDao = AppDatabase.getDatabase(application).articleDao()
                val articleListHome = articleListDao.getArticleList(HOME)
                val articleListTop = articleListDao.getTopArticleList(HOME_TOP)
                var downTopArticleTime = 0L

                dataStore.readLongFlow(DOWN_TOP_ARTICLE_TIME, System.currentTimeMillis()).first {
                    downTopArticleTime = it
                    true
                }
                if (articleListTop.isNotEmpty() && downTopArticleTime > 0 &&
                    downTopArticleTime - System.currentTimeMillis() < FOUR_HOUR && !query.isRefresh
                ) {
                    res.addAll(articleListTop)
                } else {
                    val topArticleListDeferred =
                        async { AppNetwork.getTopArticleList() }
                    val topArticleList = topArticleListDeferred.await()

                    if (topArticleList.errorCode == 0) {
                        if (articleListTop.isNotEmpty() && articleListTop[0].link == topArticleList.data[0].link && !query.isRefresh) {
                            res.addAll(articleListTop)
                        } else {
                            res.addAll(topArticleList.data)
                            topArticleList.data.forEach {
                                it.localType = HOME_TOP
                            }
                            dataStore.saveLongData(
                                DOWN_TOP_ARTICLE_TIME,
                                System.currentTimeMillis()
                            )
                            articleListDao.deleteAll(HOME_TOP)
                            articleListDao.insertList(topArticleList.data)
                        }
                    }
                }
                if (articleListHome.isNotEmpty() && downArticleTime > 0 && downArticleTime - System.currentTimeMillis() < FOUR_HOUR
                    && !query.isRefresh
                ) {
                    res.addAll(articleListHome)
                    Result.success(res)
                } else {
                    val articleListDeferred =
                        async { AppNetwork.getArticleList(query.page - 1) }
                    val articleList = articleListDeferred.await()
                    if (articleList.errorCode == 0) {
                        if (articleListHome.isNotEmpty() && articleListHome[0].link == articleList.data.datas[0].link && !query.isRefresh) {
                            res.addAll(articleListHome)
                        } else {
                            res.addAll(articleList.data.datas)
                            articleList.data.datas.forEach {
                                it.localType = HOME
                            }
                            dataStore.saveLongData(DOWN_ARTICLE_TIME, System.currentTimeMillis())
                            articleListDao.deleteAll(HOME)
                            articleListDao.insertList(articleList.data.datas)
                        }
                        Result.success(res)
                    } else {
                        Result.failure(
                            RuntimeException(
                                "response status is ${articleList.errorCode}" + "  msg is ${articleList.errorMsg}"
                            )
                        )
                    }
                }
            } else {
                val articleListDeferred =
                    async { AppNetwork.getArticleList(query.page - 1) }
                val articleList = articleListDeferred.await()
                if (articleList.errorCode == 0) {
                    res.addAll(articleList.data.datas)
                    Result.success(res)
                } else {
                    Result.failure(
                        RuntimeException(
                            "response status is ${articleList.errorCode}" + "  msg is ${articleList.errorMsg}"
                        )
                    )
                }
            }
        }
    }

}