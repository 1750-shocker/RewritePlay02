package com.wzh.rewriteplay02.project

import androidx.lifecycle.LiveData
import com.wzh.base.view.base.BaseViewModel
import com.wzh.model.pojo.QueryArticle
import com.wzh.model.room.entity.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProjectListViewModel@Inject constructor(
    private val projectRepository: ProjectRepository
) : BaseViewModel<List<Article>, Article, QueryArticle>(){
    override fun getData(key: QueryArticle): LiveData<Result<List<Article>>> {
        return projectRepository.getProjectList(key)
    }
}