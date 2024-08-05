package com.velmurugan.personalnote.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.velmurugan.personalnote.data.entity.Settings
import com.velmurugan.personalnote.data.model.DatabaseItems
import com.velmurugan.personalnote.data.repository.RoutineRepository
import com.velmurugan.personalnote.data.repository.SettingsRepository
import com.velmurugan.personalnote.data.repository.ShoppingRepository
import com.velmurugan.personalnote.data.repository.TaskRepository
import com.velmurugan.personalnote.utils.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val routineRepository: RoutineRepository,
    private val taskRepository: TaskRepository,
    private val shoppingRepository: ShoppingRepository,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _mainEvent = MutableSharedFlow<MainEvent>()
    val mainEvent = _mainEvent.asSharedFlow()

    private val _mainUiState = MutableStateFlow(HomeUiState())
    val mainUiState = _mainUiState.asStateFlow()


    init {

        viewModelScope.launch {

            taskRepository.getTaskCount().collect {
                _mainUiState.value = _mainUiState.value.copy(
                    tasksCount = "${it.completedCount} / ${it.totalCount}",
                    isTaskCompleted = it.completedCount == it.totalCount
                )
            }

        }

        viewModelScope.launch {
            routineRepository.getTaskCount(DateUtil.getCurrentDate()).collect {
                _mainUiState.value = _mainUiState.value.copy(
                    routinesCount = "${it.completedCount} / ${it.totalCount}",
                    isRoutineCompleted = it.completedCount == it.totalCount
                )
            }
        }
        viewModelScope.launch {
            shoppingRepository.getTaskCount().collect {
                _mainUiState.value = _mainUiState.value.copy(
                    shoppingCount = "${it.completedCount} / ${it.totalCount}",
                    isShoppingCompleted = it.completedCount == it.totalCount
                )
            }
        }

        viewModelScope.launch {
            val lastSyncDate = settingsRepository.getSettings()?.lastRoutineSyncDate
            if (lastSyncDate != DateUtil.getCurrentDate()) {
                val routineList = routineRepository.getRoutineByDate(DateUtil.getPreviousDate())
                if (routineList.isNotEmpty()) {
                    routineRepository.addRoutineList(routineList.map {
                        it.copy(
                            routineId = 0,
                            isCompleted = false,
                            date = DateUtil.getCurrentDate()
                        )
                    })
                }
                settingsRepository.addSettings(Settings(lastRoutineSyncDate = DateUtil.getCurrentDate()))
            }
        }
    }

    fun backupDatabase(file: File) {
        viewModelScope.launch {
            runCatching {
                val routines = routineRepository.getRoutineList()
                val tasks = taskRepository.getAllTasks()
                val shoppingItems = shoppingRepository.getShoppingList()

                // Process data
                val databaseItems = DatabaseItems(
                    routines = routines,
                    tasks = tasks,
                    shoppingItems = shoppingItems
                )
                Gson().toJson(databaseItems)
            }.mapCatching {
                storeJsonToFile(file, it)
            }.onSuccess {
                _mainEvent.emit(MainEvent.OnBackUpSuccess(file))
            }.onFailure {
                _mainEvent.emit(MainEvent.OnError(it.message.toString()))
            }
        }
    }

    fun restoreDatabase(file: File) {
        viewModelScope.launch {
            runCatching {
                val jsonInput = file.readText()
                val gson = Gson()
                val databaseItems = gson.fromJson(jsonInput, DatabaseItems::class.java)
                routineRepository.cleanRoutines()
                taskRepository.cleanTasks()
                shoppingRepository.cleanShopping()
                routineRepository.addRoutineList(databaseItems.routines)
                taskRepository.addTasks(databaseItems.tasks)
                shoppingRepository.addMultipleShoppingItems(databaseItems.shoppingItems)
            }.onSuccess {

            }.onFailure {

            }
        }
    }


    fun getMenuItems(screen: String) : List<String> {
        return when(screen) {
            HOME -> {
                listOf("Backup", "Restore")
            }
            ROUTINE -> {
                listOf("Reset Routine")
            }

            TASK -> {
                listOf("Reset Task")
            }
            SHOPPING -> {
                listOf("Clear Shopping")
            }
            else -> {
                listOf()
            }
        }
    }

    fun resetRoutine() {
        routineRepository.resetRoutines()
    }

    fun resetTask() {
        taskRepository.resetTasks()
    }

    fun resetShopping() {
        shoppingRepository.cleanShopping()
    }

}

suspend fun storeJsonToFile(file: File, jsonString: String) {
    //  val file = File(context.filesDir, fileName)
    try {
        file.writeText(jsonString)
        // Handle success, e.g., show a toast message
    } catch (e: Exception) {
        // Handle exception, e.g., log the error
    }
}


sealed class MainEvent {
    data class OnBackUpSuccess(val file: File) : MainEvent()
    data class OnError(val message: String) : MainEvent()
}


data class HomeUiState(
    val routinesCount: String = "",
    val isRoutineCompleted: Boolean = false,
    val tasksCount: String = "",
    val isTaskCompleted: Boolean = false,
    val shoppingCount: String = "",
    val isShoppingCompleted: Boolean = false,
) {

}


