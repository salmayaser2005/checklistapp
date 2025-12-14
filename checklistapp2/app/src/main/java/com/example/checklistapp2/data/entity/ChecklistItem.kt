package com.checklistapp2.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "checklist_items")
data class ChecklistItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String,
    val checklistId: Long,
    val isChecked: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)
