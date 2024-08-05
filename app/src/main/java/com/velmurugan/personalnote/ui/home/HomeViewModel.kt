package com.velmurugan.personalnote.ui.home

import android.content.Context
import android.util.Log
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import com.velmurugan.personalnote.data.model.DatabaseItems
import com.velmurugan.personalnote.data.model.TaskCount
import com.velmurugan.personalnote.data.repository.RoutineRepository
import com.velmurugan.personalnote.data.repository.ShoppingRepository
import com.velmurugan.personalnote.data.repository.TaskRepository
import com.velmurugan.personalnote.utils.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import okhttp3.internal.wait
import java.io.File
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val routineRepository: RoutineRepository,
    private val taskRepository: TaskRepository,
    private val shoppingRepository: ShoppingRepository
) : ViewModel() {


    private val _homeUiState = MutableStateFlow(HomeUiState())
    val homeUiState = _homeUiState.asStateFlow()



    init {

        viewModelScope.launch {

            taskRepository.getTaskCount().collect {
                _homeUiState.value = _homeUiState.value.copy(
                    tasksCount = it,
                    isTaskCompleted = it.completedCount == it.totalCount
                )
            }

        }

        viewModelScope.launch {
            routineRepository.getTaskCount(DateUtil.getCurrentDate()).collect {
                _homeUiState.value = _homeUiState.value.copy(
                    routinesCount = it,
                    isRoutineCompleted = it.completedCount == it.totalCount
                )
            }
        }
        viewModelScope.launch {
            shoppingRepository.getTaskCount().collect {
                _homeUiState.value = _homeUiState.value.copy(
                    shoppingCount = it,
                    isShoppingCompleted = it.completedCount == it.totalCount
                )
            }
        }


    }



}





data class HomeUiState(
    val routinesCount: TaskCount = TaskCount(0,0),
    val isRoutineCompleted: Boolean = false,
    val tasksCount: TaskCount = TaskCount(0,0),
    val isTaskCompleted: Boolean = false,
    val shoppingCount: TaskCount = TaskCount(0,0),
    val isShoppingCompleted: Boolean = false
)

