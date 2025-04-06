package com.wzh.network.service

import com.wzh.model.model.ArticleList
import com.wzh.model.model.BaseModel
import com.wzh.model.room.entity.ProjectTab
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ProjectService {
    @GET("project/tree/json")
    suspend fun getProjectTab(): BaseModel<List<ProjectTab>>

    @GET("project/list/{page}/json")
    suspend fun getProjectList(@Path("page") page: Int, @Query("cid") cid: Int): BaseModel<ArticleList>

}