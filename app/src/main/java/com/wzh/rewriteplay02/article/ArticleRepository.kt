package com.wzh.rewriteplay02.article

import android.app.Application
import com.wzh.base.Play
import com.wzh.base.util.showToast
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@ActivityRetainedScoped
class ArticleRepository @Inject constructor(val application: Application):
    CoroutineScope by MainScope(){
     suspend fun setCollect(
         isCollect:Int,
         pageId:Int,
         originId:Int,
         collectListener:(Boolean) -> Unit
     ){
         coroutineScope{
             launch{
                 Play.isLogin().collectLatest{
                     if(!it){
                         application.showToast(com.wzh.base.R.string.not_currently_logged_in)
                         return@collectLatest
                     }
                 }
             }
         }
         if(isCollect ==-1 || pageId==-1){
             application.showToast(com.wzh.base.R.string.page_is_not_collection)
             return
         }
         val
     }
}