package com.checklistapp2.app.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.checklistapp2.app.data.entity.User
import com.checklistapp2.app.data.repository.UserRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class UserViewModel(private val repository: UserRepository) : ViewModel() {

    val user: StateFlow<User?> = repository.getUser()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun createOrUpdateUser(name: String, email: String, avatarUri: String?) {
        viewModelScope.launch {
            val currentUser = user.value
            if (currentUser == null) {
                repository.insertUser(User(name = name, email = email, avatarUri = avatarUri))
            } else {
                repository.updateUser(currentUser.copy(name = name, email = email, avatarUri = avatarUri))
            }
        }
    }

    fun updateAvatar(avatarUri: String) {
        viewModelScope.launch {
            user.value?.let { currentUser ->
                repository.updateUser(currentUser.copy(avatarUri = avatarUri))
            }
        }
    }
}
