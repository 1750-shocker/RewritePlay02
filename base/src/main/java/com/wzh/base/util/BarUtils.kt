package com.wzh.base.util

import android.app.Activity
import androidx.core.view.WindowCompat

/**
 * 设置透明状态栏
 */
fun Activity.transparentStatusBar() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
}