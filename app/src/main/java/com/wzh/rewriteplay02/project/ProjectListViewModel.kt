package com.wzh.rewriteplay02.project

import androidx.lifecycle.LiveData
import com.wzh.base.view.base.BaseAndroidViewModel
import com.wzh.model.pojo.QueryArticle
import com.wzh.model.room.entity.Article
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProjectListViewModel@Inject constructor(
    private val projectRepository: ProjectRepository
) : BaseAndroidViewModel<List<Article>, Article, QueryArticle>(){
    override fun getData(page: QueryArticle): LiveData<Result<List<Article>>> {
        //TODO:调用仓库的数据请求方法，返回的是包含Result的livedata，Result包含集合，集合里面是Article
        return projectRepository.getProjectList(page)
    }
}