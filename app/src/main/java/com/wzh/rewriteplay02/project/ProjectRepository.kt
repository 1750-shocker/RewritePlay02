package com.wzh.rewriteplay02.project

import androidx.lifecycle.LiveData
import com.wzh.model.pojo.QueryArticle
import com.wzh.model.room.entity.Article
import com.wzh.model.room.entity.ProjectClassifyTab

class ProjectRepository {
    fun getProjectList(page: QueryArticle): LiveData<Result<List<Article>>> {
        //TODO:网络请求
        return null!!
    }
    fun getProjectTab(isRefresh: Boolean): LiveData<Result<List<ProjectClassifyTab>>> {
        return null!!
    }
}