package com.wzh.rewriteplay02.project

import androidx.lifecycle.LiveData
import com.wzh.base.view.base.BaseAndroidViewModel
import com.wzh.model.room.entity.ProjectClassifyTab
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(
    private val projectRepository: ProjectRepository
) : BaseAndroidViewModel<List<ProjectClassifyTab>, Unit, Boolean>() {

    var position=0

    init{
        //一上来就要请求数据
        getDataList(false)
    }

    override fun getData(page: Boolean): LiveData<Result<List<ProjectClassifyTab>>> {
        //设置具体的数据请求方法
        return projectRepository.getProjectTab(page)
    }
}