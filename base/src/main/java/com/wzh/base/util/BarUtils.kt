package com.wzh.base.util

import android.app.Activity
import android.content.Context
import android.view.View
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat.Type.ime

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

/**
 * 隐藏ime
 */
fun Activity?.hideIme(currentFocusView: View? = null) {
    if (this == null || window == null) return
    val view = currentFocusView ?: window.decorView
    view.clearFocus()
    val controller = WindowCompat.getInsetsController(window, view)
    controller.hide(ime())
}