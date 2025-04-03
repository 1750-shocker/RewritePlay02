package com.wzh.base.util

import android.app.Activity
import android.content.Context
import androidx.core.view.WindowCompat

/**
 * 设置透明状态栏
 */
fun Activity.transparentStatusBar() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
}

fun Context?.getStatusBarHeight(): Int {
    var result = 60
    if (this == null) return result
    val resId = resources.getIdentifier("status_bar_height", "dimen", "android")
//    val resId = resources.getDimensionPixelSize(android.R.dimen.thumbnail_height)
    if (resId > 0) {
        result = resources.getDimensionPixelOffset(resId)
    }
    return result
}