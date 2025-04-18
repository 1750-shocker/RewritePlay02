package com.wzh.rewriteplay02.profile.login

import com.wzh.network.base.AppNetwork
import com.wzh.rewriteplay02.base.liveDataModel
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class AccountRepository @Inject constructor() {
    suspend fun getLogin(username: String, password: String) =
        AppNetwork.getLogin(username, password)

    suspend fun getRegister(username: String, password: String, repassword: String) =
        AppNetwork.getRegister(username, password, repassword)

    fun getLogout() = liveDataModel { AppNetwork.getLogout() }
}