package com.wzh.model.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.wzh.model.room.entity.ProjectTab

@Dao
interface ProjectTabDao {
    @Query("SELECT * FROM project_classify where order_classify>144999 and order_classify<145050")
    suspend fun getProjectTabList():List<ProjectTab>

    @Query("SELECT * FROM project_classify where order_classify>189999 and order_classify<190020")
    suspend fun getAllOfficial(): List<ProjectTab>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertList(projectTabList: List<ProjectTab>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(projectTab: ProjectTab)

    @Delete
    suspend fun delete(projectTab: ProjectTab): Int

    @Delete
    suspend fun deleteList(projectTabList: List<ProjectTab>): Int

    @Query("DELETE FROM project_classify")
    suspend fun deleteAll()
}