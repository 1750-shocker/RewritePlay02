package com.wzh.model.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.wzh.model.room.entity.BannerBean

@Dao
interface BannerBeanDao {

    @Query("SELECT * FROM banner_bean order by uid desc")
    suspend fun getBannerBeanList(): List<BannerBean>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertList(BannerBeanList: List<BannerBean>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(BannerBean: BannerBean)

    @Update
    suspend fun update(BannerBean: BannerBean): Int

    @Delete
    suspend fun delete(BannerBean: BannerBean): Int

    @Delete
    suspend fun deleteList(BannerBeanList: List<BannerBean>): Int

    @Query("DELETE FROM banner_bean")
    suspend fun deleteAll()
}
