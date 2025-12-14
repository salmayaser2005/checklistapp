package com.checklistapp2.app.data.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.checklistapp2.app.data.entity.Checklist
import com.checklistapp2.app.data.entity.ChecklistItem

data class ChecklistWithItems(
    @Embedded val checklist: Checklist,
    @Relation(
        parentColumn = "id",
        entityColumn = "checklistId"
    )
    val items: List<ChecklistItem>
)
