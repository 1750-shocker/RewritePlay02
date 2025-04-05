package com.wzh.model.model

data class BaseModel<T>(
    val `data`: T,
    val errorCode: Int,
    val errorMsg: String
)