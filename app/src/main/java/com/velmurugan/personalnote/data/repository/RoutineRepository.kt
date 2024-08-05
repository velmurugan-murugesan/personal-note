package com.velmurugan.personalnote.data.repository

import com.velmurugan.personalnote.data.dao.RoutineDao
import com.velmurugan.personalnote.data.entity.Routine
import com.velmurugan.personalnote.data.model.TaskCount
import kotlinx.coroutines.flow.Flow


class RoutineRepository(
    private val routineDao: RoutineDao
) {
    suspend fun addRoutine(routine: Routine) {
        routineDao.insertRoutine(routine)
    }

    suspend fun addRoutineList(routines: List<Routine>) {
        routineDao.insertRoutine(routines)
    }

    suspend fun updateRoutine(routine: Routine) {
        routineDao.updateRoutine(routine)
    }

    suspend fun deleteRoutine(routine: Routine) {
        routineDao.deleteRoutine(routine)
    }

    fun getRoutineById(routineId: Int): Flow<Routine> {
        return routineDao.getRoutineById(routineId)
    }

    fun getAllRoutines(): Flow<List<Routine>> {
        return routineDao.getAllRoutines()
    }

    fun getRoutineList() = routineDao.getRoutinesList()

    fun getTaskCount(date: String) : Flow<TaskCount> {
        return routineDao.getTaskCounts(date)
    }

    fun resetRoutines() = routineDao.setAllRoutineIsCompletedToFalse()

    fun cleanRoutines() = routineDao.clearRoutines()

    fun getRoutineByDate(date: String) : List<Routine> {
        return routineDao.getRoutineByDate(date)
    }

    fun getLast7DaysRoutines(dates: List<String>): Flow<List<Routine>> {
       return routineDao.getLast7DaysRoutines(dates)
    }

}