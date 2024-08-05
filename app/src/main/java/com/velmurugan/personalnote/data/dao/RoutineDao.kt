package com.velmurugan.personalnote.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.velmurugan.personalnote.data.entity.Routine
import com.velmurugan.personalnote.data.model.TaskCount
import kotlinx.coroutines.flow.Flow


@Dao
interface RoutineDao {

    @Query("SELECT * FROM Routine")
    fun getAllRoutines(): Flow<List<Routine>>

    @Query("SELECT * FROM Routine")
    fun getRoutinesList(): List<Routine>

    @Query("SELECT * FROM Routine WHERE routineId = :routineId")
    fun getRoutineById(routineId: Int): Flow<Routine>

    @Query("SELECT * FROM Routine WHERE routineName LIKE :query")
    fun searchRoutines(query: String): Flow<List<Routine>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: Routine)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRoutine(routine: List<Routine>)

    @Update
    suspend fun updateRoutine(routine: Routine)

    @Delete
    suspend fun deleteRoutine(routine: Routine)

    @Query("SELECT COUNT(*) AS totalCount, SUM(CASE WHEN isCompleted THEN 1 ELSE 0 END) AS completedCount FROM Routine WHERE date = :date")
    fun getTaskCounts(date: String): Flow<TaskCount>

    @Query("DELETE FROM Routine")
    fun clearRoutines()

    @Query("UPDATE Routine SET isCompleted = 0")
    fun setAllRoutineIsCompletedToFalse()

    @Query("SELECT * FROM Routine WHERE date = :date")
    fun getRoutineByDate(date: String) : List<Routine>

    @Query("SELECT * FROM Routine WHERE date IN (:dateList)")
    fun getLast7DaysRoutines(dateList: List<String>): Flow<List<Routine>>


}