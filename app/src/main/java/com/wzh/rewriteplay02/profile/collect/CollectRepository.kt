package com.wzh.rewriteplay02.profile.collect

import com.wzh.network.base.AppNetwork
import com.wzh.rewriteplay02.base.liveDataModel
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CollectRepository @Inject constructor() {
    fun getCollectList(page: Int) = liveDataModel { AppNetwork.getCollectList(page) }
    suspend fun cancelCollects(id: Int) = AppNetwork.cancelCollect(id)
    suspend fun toCollects(id: Int) = AppNetwork.toCollect(id)
}

@EntryPoint
@InstallIn(SingletonComponent::class)
interface CollectRepositoryEntryPoint {
    fun getCollectRepository(): CollectRepository
}