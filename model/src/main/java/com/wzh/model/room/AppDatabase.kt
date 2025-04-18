package com.wzh.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wzh.model.room.dao.ArticleDao
import com.wzh.model.room.dao.BannerBeanDao
import com.wzh.model.room.dao.HotKeyDao
import com.wzh.model.room.dao.ProjectTabDao
import com.wzh.model.room.entity.Article
import com.wzh.model.room.entity.BannerBean
import com.wzh.model.room.entity.HotKey
import com.wzh.model.room.entity.ProjectTab


@Database(entities = [ProjectTab::class, Article::class , HotKey::class, BannerBean::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getDatabase(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

    abstract fun projectTabDao(): ProjectTabDao
    abstract fun articleDao(): ArticleDao
    abstract fun bannerBeanDao(): BannerBeanDao
    abstract fun hotKeyDao(): HotKeyDao

}