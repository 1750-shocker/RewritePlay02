package com.wzh.rewriteplay02.base

import android.util.Log
import androidx.lifecycle.liveData
import com.wzh.model.model.BaseModel


private const val TAG = "LiveDataUtils"

fun <T> liveDataModel(block: suspend () -> BaseModel<T>) =
    liveData {
        val result = try {
            val baseModel = block()
            if (baseModel.errorCode == 0) {
                val model = baseModel.data
                Result.success(model)
            } else {
                Log.e(
                    TAG,
                    "fires:response status is ${baseModel.errorCode} msg is ${baseModel.errorMsg}",
                )
                Result.failure(RuntimeException(baseModel.errorMsg))
            }
        } catch (e: Exception) {
            Log.e(TAG, "liveDataModel: ${e.toString()}")
            Result.failure(e)
        }
        emit(result)
    }

fun <T> liveDataFire(block: suspend () -> Result<T>) =
    liveData {
        val result = try {
            block()
        } catch (e: Exception) {
            Log.e(TAG, "liveDataFire: $e")
            Result.failure(e)
        }
        emit(result)
    }