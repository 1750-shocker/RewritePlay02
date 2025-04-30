package com.wzh.rewriteplay02.article

import android.app.Application
import android.util.Log
import com.wzh.base.Play
import com.wzh.base.util.showToast
import com.wzh.rewriteplay02.profile.collect.CollectRepositoryEntryPoint
import dagger.hilt.android.EntryPointAccessors
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ActivityRetainedScoped
class ArticleRepository @Inject constructor(private val application: Application) :
    CoroutineScope by MainScope() {

    suspend fun setCollect(
        isCollect: Int,
        pageId: Int,
        originId: Int,
        collectListener: (Boolean) -> Unit
    ) {
        Log.d("wzhhhh", "setCollect: 执行了ArticleRepository中的请求")
        /*coroutineScope {
            launch {
                Play.isLogin().collectLatest {
                    if (!it) {
                        application.showToast(com.wzh.base.R.string.not_currently_logged_in)
                        Log.d("wzhhhh", "setCollect: 貌似直接没通过登录判断，直接返回掉了")
                        return@collectLatest
                    }
                }
            }
        }*/
        Log.d("wzhhhh", "setCollect: 登录的协程进去了能出来")
        if (isCollect == -1 || pageId == -1) {
            Log.d("wzhhhh", "setCollect: 非文章详情使用或者不可收藏没通过")
            application.showToast(com.wzh.base.R.string.page_is_not_collection)
            return
        }
        Log.d("wzhhhh", "setCollect: 来咯")
        val collectRepository = EntryPointAccessors.fromApplication(
            application,
            CollectRepositoryEntryPoint::class.java
        ).getCollectRepository()

        withContext(Dispatchers.IO) {
            Log.d("wzhhhh", "进来了，进到仓库调用的协程中了")
            if (isCollect == 1) {
                val cancelCollects =
                    collectRepository.cancelCollects(if (originId != -1) originId else pageId)
                Log.d("wzhhhh", "取消收藏成功！errorCode==${cancelCollects.errorCode}")
                if (cancelCollects.errorCode == 0) {
//                    application.showToast(com.wzh.base.R.string.collection_cancelled_successfully)
                    //WHY:接收者在ArticleCollectxxx中，设置的回调是数据刷新
                    ArticleBroadCast.sendArticleChangeReceiver(application)
                    withContext(Dispatchers.Main){
                        collectListener.invoke(false)
                    //BUG：这个地方的逻辑传进来一些UI操作，需要切换回主线程，源代码这里还是IO线程不对
                    }

                } else {
                    application.showToast(com.wzh.base.R.string.failed_to_cancel_collection)
                }
            } else {
                Log.d("wzhhhh", "setCollect: 这里进来了，我点了菜单收藏，但是没反应，没toast")
                val toCollects = collectRepository.toCollects(pageId)
                Log.d("wzhhhh", "去收藏成功！errorCode=${toCollects.errorCode}")
                if (toCollects.errorCode == 0) {
//                    application.showToast(com.wzh.base.R.string.collection_successful)
                    ArticleBroadCast.sendArticleChangeReceiver(application)
                    withContext(Dispatchers.Main){
                        collectListener.invoke(true)
                    }
                } else {
                    application.showToast(com.wzh.base.R.string.collection_failed)
                }
            }
        }
    }
}