package com.wzh.model.room.entity

data class ProjectClassifyTab(
    val uid: Int,
    val courseId: Int,
    val id: Int,
    val name: String,
    val order: Int,
    val parentChapterId: Int,
    val userControlSetTop: Boolean,
    val visible: Int
)
