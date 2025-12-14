package com.checklistapp2.app.data.repository

import com.checklistapp2.app.data.dao.UserDao
import com.checklistapp2.app.data.entity.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    fun getUser(): Flow<User?> = userDao.getUser()

    suspend fun insertUser(user: User): Long = userDao.insertUser(user)

    suspend fun updateUser(user: User) = userDao.updateUser(user)

    suspend fun deleteUser(user: User) = userDao.deleteUser(user)
}
