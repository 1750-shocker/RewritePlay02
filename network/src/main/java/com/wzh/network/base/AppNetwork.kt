package com.wzh.network.base

import com.wzh.network.service.CollectService
import com.wzh.network.service.HomePageService
import com.wzh.network.service.LoginService
import com.wzh.network.service.OfficialService
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

    private val officialService = ServiceCreator.create(OfficialService::class.java)
    suspend fun getWxArticleTree() = officialService.getWxArticleTree()
    suspend fun getWxArticle(page: Int, cid: Int) = officialService.getWxArticle(page, cid)

    private val loginService = ServiceCreator.create(LoginService::class.java)
    suspend fun getLogin(username: String, password: String) =
        loginService.getLogin(username, password)

    suspend fun getRegister(username: String, password: String, repassword: String) =
        loginService.getRegister(username, password, repassword)

    suspend fun getLogout() = loginService.getLogout()

    private val homePageService = ServiceCreator.create(HomePageService::class.java)
    suspend fun getBanner() = homePageService.getBanner()
    suspend fun getTopArticleList() = homePageService.getTopArticle()
    suspend fun getArticleList(page: Int) = homePageService.getArticle(page)
    suspend fun getHotKey() = homePageService.getHotKey()
    suspend fun getQueryArticleList(page: Int, k: String) =
        homePageService.getQueryArticleList(page, k)
}