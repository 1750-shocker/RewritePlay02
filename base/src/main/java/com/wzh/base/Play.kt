package com.wzh.base

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import com.wzh.base.util.DataStoreUtils
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


@SuppressLint("StaticFieldLeak")
object Play {
    private const val TAG = "Play"
    private const val USERNAME = "username"
    private const val NICK_NAME = "nickname"
    private const val IS_LOGIN = "isLogin"
    private lateinit var dataStore: DataStoreUtils

    /**
     * 获取全局Context
     */
    var context: Context? =null
        private set

    /**
     * 初始化,传入Application的context
     */
    fun initialize(c: Context?){
        if(c==null){
            Log.w(TAG, "initailize: context is null")
            return
        }
        context = c
        context?.apply{
            dataStore = DataStoreUtils.init(this)
        }
    }

    /**
     * 判断用户是否已登录。
     *
     * @return 已登录返回true，未登录返回false。
     */
    fun isLogin(): Flow<Boolean> {
        return if (::dataStore.isInitialized) {
            dataStore.readBooleanFlow(IS_LOGIN)
        } else {
            flow {
                emit(false)
            }
        }
    }
}