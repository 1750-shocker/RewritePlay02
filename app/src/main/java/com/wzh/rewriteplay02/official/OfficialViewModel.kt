package com.wzh.rewriteplay02.official

import androidx.lifecycle.LiveData
import com.wzh.base.view.base.BaseViewModel
import com.wzh.model.room.entity.ProjectTab
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class OfficialViewModel @Inject constructor(
    private val officialRepository: OfficialRepository
) : BaseViewModel<List<ProjectTab>, Unit, Boolean>() {
    var position = 0

    override fun getData(key: Boolean): LiveData<Result<List<ProjectTab>>> {
        return officialRepository.getWxArticleTree(key)
    }

    init {
        getDataList(false)
    }
}