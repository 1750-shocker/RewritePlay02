package com.wzh.network.base

import com.wzh.network.service.CollectService
import com.wzh.network.service.ProjectService
import com.wzh.network.service.ShareService

object AppNetwork {
    private val projectService = ServiceCreator.create(ProjectService::class.java)
    suspend fun getProjectTab() = projectService.getProjectTab()
    suspend fun getProjectList(page: Int, cid: Int) = projectService.getProjectList(page, cid)

    private val collectService = ServiceCreator.create(CollectService::class.java)
    suspend fun getCollectList(page: Int) = collectService.getCollectList(page)
    suspend fun toCollect(id: Int) = collectService.toCollect(id)
    suspend fun cancelCollect(id: Int) = collectService.cancelCollect(id)

    private val shareService = ServiceCreator.create(ShareService::class.java)
    suspend fun getMyShareList(page: Int) = shareService.getMyShareList(page)
    suspend fun getShareList(cid: Int, page: Int) = shareService.getShareList(cid, page)
    suspend fun deleteMyArticle(cid: Int) = shareService.deleteMyArticle(cid)
    suspend fun shareArticle(title: String, link: String) = shareService.shareArticle(title, link)
}