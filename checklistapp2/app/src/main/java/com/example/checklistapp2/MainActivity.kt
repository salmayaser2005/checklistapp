package com.checklistapp2.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
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
    entities = [Checklist::class, ChecklistItem::class, User::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun checklistDao(): ChecklistDao
    abstract fun checklistItemDao(): ChecklistItemDao
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "checklist_db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
