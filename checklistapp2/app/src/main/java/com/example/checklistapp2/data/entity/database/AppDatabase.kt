package com.checklistapp2.app.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.checklistapp2.app.data.dao.ChecklistDao
import com.checklistapp2.app.data.dao.ChecklistItemDao
import com.checklistapp2.app.data.dao.UserDao
import com.checklistapp2.app.data.entity.Checklist
import com.checklistapp2.app.data.entity.ChecklistItem
import com.checklistapp2.app.data.entity.User
import com.checklistapp2.app.data.util.Converters

@Database(
    entities = [
        Checklist::class,
        ChecklistItem::class,
        User::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun checklistDao(): ChecklistDao

    abstract fun checklistItemDao(): ChecklistItemDao

    abstract fun userDao(): UserDao
}
