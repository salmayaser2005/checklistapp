package com.checklistapp2.app.data.dao

import androidx.room.*
import com.checklistapp2.app.data.entity.ChecklistItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ChecklistItemDao {

    @Query("SELECT * FROM checklist_items WHERE checklistId = :checklistId")
    fun getItemsForChecklist(checklistId: Long): Flow<List<ChecklistItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertItem(item: ChecklistItem)

    @Update
    suspend fun updateItem(item: ChecklistItem)

    @Delete
    suspend fun deleteItem(item: ChecklistItem)

    @Query("UPDATE checklist_items SET isChecked = :isChecked WHERE id = :itemId")
    suspend fun updateItemCheckedStatus(itemId: Long, isChecked: Boolean)
}
