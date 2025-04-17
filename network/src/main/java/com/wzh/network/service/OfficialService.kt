package com.wzh.network.service

import com.wzh.model.model.ArticleList
import com.wzh.model.model.BaseModel
import com.wzh.model.room.entity.ProjectTab
import retrofit2.http.GET
import retrofit2.http.Path


interface OfficialService {
    @GET("wxarticle/chapters/json")
    suspend fun getWxArticleTree(): BaseModel<List<ProjectTab>>

    @GET("wxarticle/list/{cid}/{page}/json")
    suspend fun getWxArticle(@Path("page") page: Int, @Path("cid") cid: Int): BaseModel<ArticleList>
}