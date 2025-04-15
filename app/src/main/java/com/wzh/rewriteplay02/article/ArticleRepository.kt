package com.wzh.rewriteplay02.article

import android.app.Application
import com.wzh.base.Play
import com.wzh.base.util.showToast
import com.wzh.rewriteplay02.article.collect.CollectRepositoryEntryPoint
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
class ArticleRepository @Inject constructor(val application: Application) :
    CoroutineScope by MainScope() {

    suspend fun setCollect(
        isCollect: Int,
        pageId: Int,
        originId: Int,
        collectListener: (Boolean) -> Unit
    ) {
        coroutineScope {
            launch {
                Play.isLogin().collectLatest {
                    if (!it) {
                        application.showToast(com.wzh.base.R.string.not_currently_logged_in)
                        return@collectLatest
                    }
                }
            }
        }
        if (isCollect == -1 || pageId == -1) {
            application.showToast(com.wzh.base.R.string.page_is_not_collection)
            return
        }
        val collectRepository = EntryPointAccessors.fromApplication(
            application,
            CollectRepositoryEntryPoint::class.java
        ).getCollectRepository()

        withContext(Dispatchers.IO) {
            if (isCollect == 1) {
                val cancelCollects =
                    collectRepository.cancelCollects(if (originId != -1) originId else pageId)
                if (cancelCollects.errorCode == 0) {
                    application.showToast(com.wzh.base.R.string.collection_cancelled_successfully)
                    ArticleBroadCast.sendArticleChangeReceiver(application)
                    collectListener.invoke(false)
                } else {
                    application.showToast(com.wzh.base.R.string.failed_to_cancel_collection)
                }
            } else {
                val toCollects = collectRepository.toCollects(pageId)
                if (toCollects.errorCode == 0) {
                    application.showToast(com.wzh.base.R.string.collection_successful)
                    ArticleBroadCast.sendArticleChangeReceiver(application)
                    collectListener.invoke(true)
                } else {
                    application.showToast(com.wzh.base.R.string.collection_failed)
                }
            }
        }
    }
}