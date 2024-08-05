package com.velmurugan.personalnote.ui.routine

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velmurugan.personalnote.data.entity.Routine
import com.velmurugan.personalnote.data.model.TaskCount
import com.velmurugan.personalnote.data.repository.RoutineRepository
import com.velmurugan.personalnote.utils.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class RoutineViewModel @Inject constructor(
    private val routineRepository: RoutineRepository
) : ViewModel() {

    private val _routineUiState = MutableStateFlow(RoutineUiState())
    val routineUiState = _routineUiState.asStateFlow()

    private val _routeEvent = MutableSharedFlow<RoutineEvent>()
    val routeEvent = _routeEvent.asSharedFlow()


    init {
        viewModelScope.launch {
            routineRepository.getLast7DaysRoutines(
                DateUtil.getLast7Days().map { it }
            ).collect {
                val currentDateRoutines =
                    it.filter { it.date == _routineUiState.value.selectedRoutineDay }
                _routineUiState.value = _routineUiState.value.copy(
                    last7DaysRoutines = it,
                    selectedDateRoutines = currentDateRoutines,
                    taskCount = TaskCount(
                        currentDateRoutines.count { it.isCompleted },
                        currentDateRoutines.size
                    )
                )
            }
        }

        viewModelScope.launch {
            routineRepository.getTaskCount(DateUtil.getCurrentDate()).collect {
                _routineUiState.value = _routineUiState.value.copy(taskCount = it)
            }
        }

        viewModelScope.launch {
            _routineUiState.value = _routineUiState.value.copy(
                isShowAddRoutine = _routineUiState.value.selectedRoutineDay == DateUtil.getCurrentDate()
            )
        }
    }

    fun addRoutine() {
        if (routineUiState.value.selectedRoutine == null) {
            viewModelScope.launch {
                runCatching {
                    check(routineUiState.value.newRouteName.isNotEmpty()) {
                        "Routine name cannot be empty"
                    }

                    val newRouteName =
                        routineUiState.value.newRouteName.split(",").map { it.trim() }
                            .filter { it.isNotBlank() }
                    val routineList = newRouteName.map {
                        Routine(
                            routineName = it,
                            date = DateUtil.getCurrentDate(),
                            isCompleted = false
                        )
                    }

                    routineRepository.addRoutineList(routineList)
                }.onSuccess {
                    _routineUiState.value = _routineUiState.value.copy(newRouteName = "")
                    _routeEvent.emit(RoutineEvent.HideAddRouteDialog)
                }.onFailure {

                }

            }
        } else {
            viewModelScope.launch {
                routineUiState.value.selectedRoutine!!.copy(
                    routineName = routineUiState.value.newRouteName
                ).also {
                    routineRepository.updateRoutine(it)

                    _routineUiState.value = _routineUiState.value.copy(
                        selectedRoutine = null,
                        newRouteName = ""
                    )
                    _routeEvent.emit(RoutineEvent.HideAddRouteDialog)
                }
            }

        }

    }

    fun updateRouteIsCompleted(routine: Routine, isCompleted: Boolean) {
        viewModelScope.launch {
            routineRepository.updateRoutine(
                routine.copy(
                    isCompleted = isCompleted
                )
            )
        }
    }


    fun deleteRoutine(routine: Routine) {
        viewModelScope.launch {
            routineRepository.deleteRoutine(
                routine
            )
        }
    }

    fun updateRoutedName(it: String) {
        _routineUiState.value = _routineUiState.value.copy(newRouteName = it)
    }

    fun updateSelectedDay(date: String) {
        _routineUiState.value = _routineUiState.value.copy(
            selectedRoutineDay = date,
            selectedDateRoutines = routineUiState.value.last7DaysRoutines.filter { it.date == date },
            isShowAddRoutine = date == DateUtil.getCurrentDate()
        )
    }

    fun updateSelectedRoutine(routine: Routine) {
        _routineUiState.value = _routineUiState.value.copy(
            selectedRoutine = routine,
            newRouteName = routine.routineName
        )
    }

}


sealed class RoutineEvent {
    object HideAddRouteDialog : RoutineEvent()
}


data class RoutineUiState(
    val selectedRoutine: Routine? = null,
    val selectedRoutineDay: String = DateUtil.getCurrentDate(),
    val selectedDateRoutines: List<Routine> = emptyList(),
    val last7DaysRoutines: List<Routine> = emptyList(),
    val newRouteName: String = "",
    val taskCount: TaskCount = TaskCount(0, 0),
    val isShowAddRoutine: Boolean = false
)
