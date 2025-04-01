package com.wzh.base.util

import android.os.Build
import androidx.annotation.ChecksSdkIntAtLeast


object AndroidVersion {
    /**
     * 判断当前设备是否是 Android 7.0（Nougat，API 24）或更高版本
     * 注解可以确保方法只在满足最低 API 版本要求时调用
     */
    @ChecksSdkIntAtLeast(api= Build.VERSION_CODES.N)
    fun hasNougat():Boolean{
        return true
    }

    /**
     * API 31及以上
     * @return 35以上true，否则false
     */
    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.VANILLA_ICE_CREAM)
    fun hasV(): Boolean {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.VANILLA_ICE_CREAM
    }
}