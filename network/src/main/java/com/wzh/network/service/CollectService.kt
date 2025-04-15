package com.wzh.network.service

import com.wzh.model.model.BaseModel
import com.wzh.model.model.Collect
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CollectService {
    @GET("lg/collect/list/{page}/json")
    suspend fun getCollectList(@Path("page") page: Int): BaseModel<Collect>

    @POST("lg/collect/{id}/json")
    suspend fun toCollect(@Path("id") id: Int): BaseModel<Any>

    @POST("lg/uncollect_originId/{id}/json")
    suspend fun cancelCollect(@Path("id") id: Int): BaseModel<Any>

}