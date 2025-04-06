package com.wzh.model.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.wzh.model.room.dao.ArticleDao
import com.wzh.model.room.dao.ProjectTabDao
import com.wzh.model.room.entity.Article
import com.wzh.model.room.entity.ProjectTab


@Database(entities = [ProjectTab::class, Article::class], version = 1)
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

}