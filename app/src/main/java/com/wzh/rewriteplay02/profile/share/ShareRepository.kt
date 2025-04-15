package com.wzh.rewriteplay02.profile.share

import com.wzh.network.base.AppNetwork
import com.wzh.rewriteplay02.base.liveDataModel
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class ShareRepository @Inject constructor() {
    fun getMyShareList(page: Int) = liveDataModel { AppNetwork.getMyShareList(page) }
    fun getShareList(cid: Int, page: Int) = liveDataModel { AppNetwork.getShareList(cid, page) }
    fun deleteMyArticle(cid: Int) = liveDataModel { AppNetwork.deleteMyArticle(cid) }
    fun shareArticle(title: String, link: String) =
        liveDataModel { AppNetwork.shareArticle(title, link) }
}