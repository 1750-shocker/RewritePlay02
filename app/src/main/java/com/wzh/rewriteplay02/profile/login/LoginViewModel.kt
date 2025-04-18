package com.wzh.rewriteplay02.profile.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.wzh.base.Play
import com.wzh.base.util.showToast
import com.wzh.model.model.Login
import com.wzh.rewriteplay02.R
import com.wzh.rewriteplay02.article.ArticleBroadCast
import com.wzh.rewriteplay02.base.http
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(
    application: Application,
    private val accountRepository: AccountRepository
) : AndroidViewModel(application) {

    private val _state = MutableLiveData<LoginState>()
    val state: LiveData<LoginState>
        get() = _state

    fun toLoginOrRegister(account: Account) {
        _state.postValue(Logging)
        if (account.isLogin) {
            login(account)
        } else {
            register(account)
        }
    }

    private fun login(account: Account) {
        viewModelScope.http(
            request = { accountRepository.getLogin(account.username, account.password) },
            response = { success(it, account.isLogin) },
            error = { _state.postValue(LoginError) }
        )
    }


    private fun register(account: Account) {
        viewModelScope.http(
            request = {
                accountRepository.getRegister(
                    account.username,
                    account.password,
                    account.password
                )
            },
            response = { success(it, account.isLogin) },
            error = { _state.postValue(LoginError) }
        )
    }

    private fun success(it: Login?, isLogin: Boolean) {
        it ?: return
        _state.postValue(LoginSuccess(it))
        Play.setLogin(true)
        Play.setUserInfo(it.nickname, it.username)
        ArticleBroadCast.sendArticleChangeReceiver(context = getApplication())
        getApplication<Application>().showToast(
            if (isLogin) getApplication<Application>().getString(com.wzh.base.R.string.login_success) else getApplication<Application>().getString(
                com.wzh.base.R.string.register_success
            )
        )
    }

}

data class Account(val username: String, val password: String, val isLogin: Boolean)
sealed class LoginState
object Logging : LoginState()
data class LoginSuccess(val login: Login) : LoginState()
object LoginError : LoginState()
