package com.zoho.news.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [NewsEntity::class],
    version = 1, exportSchema = false
)
abstract class NewsDatabase : RoomDatabase() {
    abstract val dao: NewsDao
}
