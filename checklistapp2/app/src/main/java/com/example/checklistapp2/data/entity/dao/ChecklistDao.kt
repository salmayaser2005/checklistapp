package com.checklistapp2.app.data.dao

import androidx.room.*
import com.checklistapp2.app.data.entity.Checklist
import com.checklistapp2.app.data.relation.ChecklistWithItems
import kotlinx.coroutines.flow.Flow

@Dao
interface ChecklistDao {

    @Query("SELECT * FROM checklists WHERE userId = :userId ORDER BY createdAt DESC")
    fun getAllChecklists(userId: Long): Flow<List<Checklist>>

    @Transaction
    @Query("SELECT * FROM checklists WHERE userId = :userId ORDER BY createdAt DESC")
    fun getAllChecklistsWithItems(userId: Long): Flow<List<ChecklistWithItems>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertChecklist(checklist: Checklist): Long

    @Delete
    suspend fun deleteChecklist(checklist: Checklist)
}
