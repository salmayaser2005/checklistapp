package com.checklistapp2.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.checklistapp2.app.data.repository.ChecklistRepository

class ChecklistViewModelFactory(
    private val repository: ChecklistRepository,
    private val userId: Long
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChecklistViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChecklistViewModel(repository, userId) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
