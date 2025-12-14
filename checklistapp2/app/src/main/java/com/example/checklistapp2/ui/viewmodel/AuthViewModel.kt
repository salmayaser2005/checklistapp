package com.checklistapp2.app.ui.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {

    private val auth: FirebaseAuth = FirebaseAuth.getInstance()

    var loginEmail = mutableStateOf("")
    var loginPassword = mutableStateOf("")
    var registerEmail = mutableStateOf("")
    var registerPassword = mutableStateOf("")
    var errorMessage = mutableStateOf<String?>(null)
    var isLoggedIn = mutableStateOf(false)

    // Login function
    fun login() {
        viewModelScope.launch {
            auth.signInWithEmailAndPassword(loginEmail.value, loginPassword.value)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        isLoggedIn.value = true
                        errorMessage.value = null
                    } else {
                        errorMessage.value = task.exception?.message
                    }
                }
        }
    }

    // Register function
    fun register() {
        viewModelScope.launch {
            auth.createUserWithEmailAndPassword(registerEmail.value, registerPassword.value)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        isLoggedIn.value = true
                        errorMessage.value = null
                    } else {
                        errorMessage.value = task.exception?.message
                    }
                }
        }
    }

    // Logout
    fun logout() {
        auth.signOut()
        isLoggedIn.value=false
    }
}
