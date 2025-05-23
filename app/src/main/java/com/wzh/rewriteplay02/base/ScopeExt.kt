package com.wzh.rewriteplay02.base

import android.util.Log
import com.wzh.model.model.BaseModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


/**
 * 当前协程未指定调度线程，恢复挂起的数据仍在当前线程中
 */
fun <T> CoroutineScope.http(
    request: (suspend () -> BaseModel<T>),
    response: (T?) -> Unit,
    error: (String) -> Unit = {},
    showToast: Boolean = true

): Job {
    return this.launch() {
        try {
            val result = request()
            if (result.errorCode == 0) {
                response(result.data)
            } else {
                showToast(showToast, result.errorMsg)
                error(result.errorMsg)
            }
        } catch (e: Exception) {
            showToast(showToast, e.message)
            error(e.message ?: "异常")
        }

    }
}


/**
 * 当前协程指定调度线程，恢复挂起的数据仍在当前指定的线程中
 */
fun <T> CoroutineScope.http2(
    request: (suspend () -> BaseModel<T>),
    response: (T?) -> Unit,
    dispatcher: CoroutineDispatcher = Dispatchers.Main,
    error: (String) -> Unit = {},
    showToast: Boolean = true

): Job {
    return this.launch(dispatcher) {
        try {
            val result = request()
            if (result.errorCode == 0) {
                response(result.data)
            } else {
                showToast(showToast, result.errorMsg)
                error(result.errorMsg)
            }
        } catch (e: Exception) {
            showToast(showToast, e.message)
            error(e.message ?: "异常")
        }

    }
}

private const val TAG = "ScopeExt"

private fun showToast(isShow: Boolean, msg: String?) {
    Log.e(TAG, "showToast: isShow:$isShow   msg:$msg")
}