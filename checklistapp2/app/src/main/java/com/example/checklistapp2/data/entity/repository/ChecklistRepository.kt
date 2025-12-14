package com.checklistapp2.app.data.repository

import com.checklistapp2.app.data.dao.ChecklistDao
import com.checklistapp2.app.data.dao.ChecklistItemDao
import com.checklistapp2.app.data.entity.Checklist
import com.checklistapp2.app.data.entity.ChecklistItem

class ChecklistRepository(
    private val checklistDao: ChecklistDao,
    private val itemDao: ChecklistItemDao
) {

    fun getAllChecklistsWithItems(userId: Long) =
        checklistDao.getAllChecklistsWithItems(userId)

    suspend fun insertChecklist(checklist: Checklist) =
        checklistDao.insertChecklist(checklist)

    suspend fun deleteChecklist(checklist: Checklist) =
        checklistDao.deleteChecklist(checklist)

    suspend fun insertItem(item: ChecklistItem) =
        itemDao.insertItem(item)

    suspend fun updateItem(item: ChecklistItem) =
        itemDao.updateItem(item)

    suspend fun deleteItem(item: ChecklistItem) =
        itemDao.deleteItem(item)

    suspend fun setItemChecked(itemId: Long, isChecked: Boolean) =
        itemDao.updateItemCheckedStatus(itemId, isChecked)
}
