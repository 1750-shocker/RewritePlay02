package com.wzh.rewriteplay02.article

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import androidx.localbroadcastmanager.content.LocalBroadcastManager

object ArticleBroadCast {
    const val COLLECT_RECEIVER = "com.wzh.play.COLLECT"

    fun sendArticleChangeReceiver(context: Context) {
        val intent = Intent(COLLECT_RECEIVER)
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent)
    }

    fun setArticleChangesReceiver(c: Activity, block: () -> Unit): BroadcastReceiver {
        val filter = IntentFilter()
        filter.addAction(COLLECT_RECEIVER)
        val r = ArticleBroadCastReceiver(block)
        LocalBroadcastManager.getInstance(c).registerReceiver(r, filter)
        return r
    }

    fun clearArticleChangesReceiver(c: Activity, r: BroadcastReceiver?) {
        r?.apply {
            LocalBroadcastManager.getInstance(c).unregisterReceiver(this)
        }
    }
}

private class ArticleBroadCastReceiver(val block: () -> Unit) : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        Log.e("TAG", "onReceive:${intent.action}")
        if (intent.action == ArticleBroadCast.COLLECT_RECEIVER) {
            block.invoke()
        }
    }
}