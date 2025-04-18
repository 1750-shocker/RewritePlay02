package com.wzh.model.room.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.wzh.model.room.entity.HotKey

@Dao
interface HotKeyDao {

    @Query("SELECT * FROM hot_key order by uid desc")
    suspend fun getHotKeyList(): List<HotKey>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertList(HotKeyList: List<HotKey>)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(HotKey: HotKey)

    @Update
    suspend fun update(HotKey: HotKey): Int

    @Delete
    suspend fun delete(HotKey: HotKey): Int

    @Delete
    suspend fun deleteList(HotKeyList: List<HotKey>): Int

    @Query("DELETE FROM hot_key")
    suspend fun deleteAll()
}