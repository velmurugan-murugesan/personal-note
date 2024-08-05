package com.velmurugan.personalnote.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.velmurugan.personalnote.data.entity.Task
import com.velmurugan.personalnote.data.model.TaskCount
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {

    @Query("SELECT * FROM Task")
    fun getAllTasks(): List<Task>

    @Query("SELECT * FROM Task")
    fun getTasksList(): Flow<List<Task>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: Task)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(tasks: List<Task>)

    @Update
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Query("SELECT COUNT(*) AS totalCount, SUM(CASE WHEN isCompleted THEN 1 ELSE 0 END) AS completedCount FROM Task")
    fun getTaskCount(): Flow<TaskCount>

    @Query("SELECT * FROM Task WHERE taskId = :taskId")
    fun getTaskById(taskId: Int): Flow<Task>

    @Query("SELECT * FROM Task WHERE taskName LIKE '%' || :query || '%'")
    fun searchTasks(query: String): Flow<List<Task>>

    @Query("SELECT * FROM Task WHERE isCompleted = 1")
    fun getCompletedTasks(): Flow<List<Task>>

    @Query("SELECT * FROM Task WHERE isCompleted = 0")
    fun getIncompleteTasks(): Flow<List<Task>>

    @Query("DELETE FROM Task")
    fun clearTask()

    @Query("UPDATE Task SET isCompleted = 0")
    fun resetTasks()


}