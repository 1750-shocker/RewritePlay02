package com.wzh.network.service

import com.wzh.model.model.BaseModel
import com.wzh.model.model.Login
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface LoginService {
    @POST("user/login")
    suspend fun getLogin(
        @Query("username") username: String,
        @Query("password") password: String
    ): BaseModel<Login>

    @POST("user/register")
    suspend fun getRegister(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("repassword") repassword: String
    ): BaseModel<Login>

    @GET("user/logout/json")
    suspend fun getLogout(): BaseModel<Any>

}