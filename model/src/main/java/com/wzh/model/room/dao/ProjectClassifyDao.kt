package com.wzh.model.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wzh.model.room.entity.ProjectClassify

@Dao
interface ProjectClassifyDao {
    @Query("SELECT * FROM project_classify where order_classify>144999 and order_classify<145050")
    suspend fun getAllProject():List<ProjectClassify>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertList(projectClassifyList: List<ProjectClassify>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(projectClassify: ProjectClassify)

    @Delete
    suspend fun delete(projectClassify: ProjectClassify): Int

    @Delete
    suspend fun deleteList(projectClassifyList: List<ProjectClassify>): Int

    @Query("DELETE FROM project_classify")
    suspend fun deleteAll()
}