package com.velmurugan.personalnote.ui.tasks

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velmurugan.personalnote.data.entity.Task
import com.velmurugan.personalnote.data.model.TaskCount
import com.velmurugan.personalnote.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val taskRepository: TaskRepository
) : ViewModel() {

    private val _taskUiState = MutableStateFlow(TaskUiState())
    val taskUiState = _taskUiState.asStateFlow()

    init {
        viewModelScope.launch {
            taskRepository.getTaskList().collect { tasks ->
                _taskUiState.value = _taskUiState.value.copy(taskList = tasks.sortedBy { it.isCompleted })
            }
        }

        viewModelScope.launch {
            taskRepository.getTaskCount().collect {
                _taskUiState.value = _taskUiState.value.copy(
                    taskCount = it
                )
            }
        }
    }

    fun updateAddTaskVisibility(isVisible: Boolean) {
        _taskUiState.value = taskUiState.value.copy(isAddTaskVisible = isVisible)
    }


    fun addTask() {
        if (_taskUiState.value.selectedTask == null) {
            viewModelScope.launch {
                runCatching {
                    taskRepository.addTask(
                        Task(
                            priority = _taskUiState.value.priority,
                            taskName = _taskUiState.value.taskName
                        )
                    )

                }.onSuccess {
                    _taskUiState.value = _taskUiState.value.copy(
                        taskName = "",
                        priority = ""
                    )
                }.onFailure {

                }

            }
        } else {
            viewModelScope.launch {
                _taskUiState.value.selectedTask?.let {
                    taskRepository.updateTask(it.copy(taskName = _taskUiState.value.taskName,
                        priority = _taskUiState.value.priority))
                }.also {
                    _taskUiState.value = _taskUiState.value.copy(
                        taskName = "",
                        priority = "",
                        selectedTask = null
                    )
                    updateAddTaskVisibility(false)
                }
            }
        }
    }

    fun updateTaskName(it: String) {
        _taskUiState.value = _taskUiState.value.copy(
            taskName = it
        )
    }

    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(task)
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            taskRepository.deleteTask(task)
        }
    }

    fun updatePriority(priority: String) {
        _taskUiState.value = _taskUiState.value.copy(
            priority = priority
        )
    }

    fun updateSelectedTask(task: Task) {
        _taskUiState.value = _taskUiState.value.copy(
            taskName = task.taskName,
            priority = task.priority
        )
        _taskUiState.value = _taskUiState.value.copy(selectedTask = task)
    }

}

data class TaskUiState(
    val selectedTask: Task? = null,
    val taskList: List<Task> = emptyList(),
    val isAddTaskVisible: Boolean = false,
    val taskName: String = "",
    val taskCount: TaskCount = TaskCount(0,0),
    val priority: String = ""
)