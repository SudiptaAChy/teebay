package com.teebay.appname.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.teebay.appname.features.myProduct.model.Category

@Database(entities = [Category::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
}
