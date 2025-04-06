package com.wzh.rewriteplay02.project

import androidx.lifecycle.LiveData
import com.wzh.base.view.base.BaseViewModel
import com.wzh.model.room.entity.ProjectTab
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val projectRepository: ProjectRepository
) : BaseViewModel<List<ProjectTab>, Unit, Boolean>() {

    var position=0

    init{
        //一上来就要请求数据
        getDataList(false)
    }

    override fun getData(key: Boolean): LiveData<Result<List<ProjectTab>>> {
        //设置具体的数据请求方法
        return projectRepository.getProjectTab(key)
    }
}