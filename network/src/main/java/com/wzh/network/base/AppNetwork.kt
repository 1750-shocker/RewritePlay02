package com.wzh.network.base

import com.wzh.network.service.ProjectService

object AppNetwork {
    private val projectService = ServiceCreator.create(ProjectService::class.java)

    suspend fun getProjectTab() = projectService.getProjectTab()
    suspend fun getProjectList(page: Int, cid: Int) = projectService.getProjectList(page, cid)

}