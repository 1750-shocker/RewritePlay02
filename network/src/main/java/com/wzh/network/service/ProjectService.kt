package com.wzh.network.service

import com.wzh.model.model.ArticleList
import com.wzh.model.model.BaseModel
import com.wzh.model.room.entity.ProjectClassify
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface ProjectService {
    @GET("project/tree/json")
    suspend fun getProjectClassify(): BaseModel<List<ProjectClassify>>

    @GET("project/list/{page}/json")
    suspend fun getProject(@Path("page") page: Int, @Query("cid") cid: Int): BaseModel<ArticleList>

}