package com.wzh.rewriteplay02.article.collect

import com.wzh.rewriteplay02.base.liveDataModel
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CollectRepository @Inject constructor(){
    fun getCollectList(page:Int) = liveDataModel{AppNetwork.getCollectList(page)}
}