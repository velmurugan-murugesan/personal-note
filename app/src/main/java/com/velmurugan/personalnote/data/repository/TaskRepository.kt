package com.velmurugan.personalnote.data.repository

import com.velmurugan.personalnote.data.dao.TaskDao
import com.velmurugan.personalnote.data.entity.Task
import com.velmurugan.personalnote.data.model.TaskCount
import kotlinx.coroutines.flow.Flow

class TaskRepository(
    private val taskDao: TaskDao
) {
    suspend fun addTask(task: Task) {
        taskDao.insertTask(task)
    }

    suspend fun addTasks(tasks: List<Task>) {
        taskDao.insertTask(tasks)
    }

    suspend fun deleteTask(task: Task) {
        taskDao.deleteTask(task)
    }

    suspend fun updateTask(task: Task) {
        taskDao.updateTask(task)
    }

    fun getTaskCount() : Flow<TaskCount> {
        return taskDao.getTaskCount()
    }


    fun getAllTasks() = taskDao.getAllTasks()

    fun getTaskList() = taskDao.getTasksList()

    fun cleanTasks() = taskDao.clearTask()
    fun resetTasks() {
        taskDao.resetTasks()
    }
}