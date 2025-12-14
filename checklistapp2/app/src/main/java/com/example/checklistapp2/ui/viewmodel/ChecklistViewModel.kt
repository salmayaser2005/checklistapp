package com.checklistapp2.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.checklistapp2.app.data.entity.Checklist
import com.checklistapp2.app.data.entity.ChecklistCategory
import com.checklistapp2.app.data.entity.ChecklistItem
import com.checklistapp2.app.data.repository.ChecklistRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ChecklistViewModel(
    private val repository: ChecklistRepository,
    private val userId: Long
) : ViewModel() {


    val checklistsWithItems: StateFlow<List<com.checklistapp2.app.data.relation.ChecklistWithItems>> =
        repository.getAllChecklistsWithItems(userId)
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )


    fun createChecklist(
        name: String,
        category: ChecklistCategory = ChecklistCategory.PERSONAL
    ) {
        viewModelScope.launch {
            repository.insertChecklist(
                Checklist(
                    name = name,
                    category = category,
                    userId = userId
                )
            )
        }
    }

    fun deleteChecklist(checklist: Checklist) {
        viewModelScope.launch {
            repository.deleteChecklist(checklist)
        }
    }

    /* ---------------- ITEMS ---------------- */

    fun addItem(checklistId: Long, title: String) {
        viewModelScope.launch {
            repository.insertItem(
                ChecklistItem(
                    title = title,
                    checklistId = checklistId
                )
            )
        }
    }

    fun updateItem(item: ChecklistItem) {
        viewModelScope.launch {
            repository.updateItem(item)
        }
    }

    fun deleteItem(item: ChecklistItem) {
        viewModelScope.launch {
            repository.deleteItem(item)
        }
    }

    fun toggleItemChecked(itemId: Long, checked: Boolean) {
        viewModelScope.launch {
            repository.setItemChecked(itemId, checked)
        }
    }
}
