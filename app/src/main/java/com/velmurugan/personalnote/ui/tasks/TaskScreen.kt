package com.velmurugan.personalnote.ui.tasks

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.velmurugan.personalnote.data.entity.Task
import com.velmurugan.personalnote.ui.home.HorizontalProgress
import com.velmurugan.personalnote.ui.theme.PersonalNoteTheme
import com.velmurugan.personalnote.utils.Priority
import com.velmurugan.personalnote.views.BodyText
import com.velmurugan.personalnote.views.ButtonText
import com.velmurugan.personalnote.views.CaptionText
import com.velmurugan.personalnote.views.FabButton
import com.velmurugan.personalnote.views.SubtitleText
import com.velmurugan.personalnote.views.SwipeMenuItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    taskViewModel: TaskViewModel = hiltViewModel()
) {

    val taskUiState by taskViewModel.taskUiState.collectAsState()
    val priorityList = Priority.entries.toList()
    val modalBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    if (taskUiState.isAddTaskVisible) {
        ModalBottomSheet(
            onDismissRequest = {
                taskViewModel.updateAddTaskVisibility(false)
            },
            sheetState = modalBottomSheetState
        ) {
            AddTaskDialogContent(priority = taskUiState.priority,
                priorityList = priorityList.map { it.displayName },
                onUpdateTaskName = {
                    taskViewModel.updateTaskName(it)
                },
                taskName = taskUiState.taskName,
                onOptionSelected = {
                    taskViewModel.updatePriority(it)
                },
                onAddTask = {
                    taskViewModel.addTask()
                    taskViewModel.updateAddTaskVisibility(false)
                },
                onCancel = {
                    taskViewModel.updateAddTaskVisibility(false)
                })
        }
    }


    Scaffold(
        floatingActionButton = {
            FabButton(text = "Add Task", onClick = {
                taskViewModel.updateAddTaskVisibility(true)
            })
        }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {
            val progress = if (taskUiState.taskCount.totalCount > 0) {
                taskUiState.taskCount.completedCount.toFloat() / taskUiState.taskCount.totalCount.toFloat()
            } else 0F

            HorizontalProgress(
                title = "Task ${taskUiState.taskCount.completedCount} of ${taskUiState.taskCount.totalCount} completed ",
                progress =  progress )
            Spacer(modifier = Modifier.height(8.dp))


            priorityList.forEach { priority ->
                /*TaskCheckboxGroup(
                    title = priority,
                    options = taskUiState.taskList.filter { it.priority == priority },
                    onOptionSelected = { task, isChecked ->
                        taskViewModel.updateTask(
                            task.copy(
                                isCompleted = isChecked
                            )
                        )
                    },
                    onDeleted = { task ->
                        taskViewModel.deleteTask(task)
                    }
                )*/

                val tasks =
                    taskUiState.taskList.filter { it.priority.equals(priority.displayName, true) }
                if(tasks.isNotEmpty()) {
                    TaskCard(title = priority.displayName,
                        tasks = taskUiState.taskList.filter {
                            it.priority.equals(
                                priority.displayName,
                                true
                            )
                        },
                        backgroundColor = priority.getPriorityColor().copy(alpha = 0.6F),
                        onOptionSelected = { task, isChecked ->
                            taskViewModel.updateTask(
                                task.copy(
                                    isCompleted = isChecked
                                )
                            )
                        },
                        onDeleted = { task ->
                            taskViewModel.deleteTask(task)
                        },
                        onEdit = {
                            taskViewModel.updateSelectedTask(it)
                            taskViewModel.updateAddTaskVisibility(true)
                        })
                }


            }
        }
    }

}


@Composable
private fun AddTaskDialogContent(
    modifier: Modifier = Modifier,
    priority: String,
    priorityList: List<String>,
    taskName: String,
    onUpdateTaskName: (String) -> Unit = {},
    onOptionSelected: (String) -> Unit = {},
    onAddTask: () -> Unit = {},
    onCancel: () -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }
        Column(
            Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            /*SubtitleText(text = "Add Task")
            Spacer(modifier = Modifier.height(8.dp))*/
            /*AppDropDownMenu(
                modifier = Modifier,
                text = priority,
                label = "Priority",
                options = priorityList,
                onOptionSelected = onOptionSelected
            )

            Spacer(modifier = Modifier.height(8.dp))*/

            OutlinedTextField(
                modifier = Modifier.fillMaxWidth().focusRequester(focusRequester),
                label = {
                    BodyText(text = "Task Name")
                },
                value = taskName, onValueChange = onUpdateTaskName,
                minLines = 3,
                maxLines = 5
            )

            Spacer(modifier = Modifier.height(4.dp))

            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
                Priority.entries.forEach {
                    FilterChip(
                        modifier = Modifier.padding(4.dp),
                        selected = priority == it.displayName, onClick = {
                            onOptionSelected(it.displayName)
                        }, label = {
                            CaptionText( modifier = Modifier.padding(vertical = 4.dp, horizontal = 6.dp), text = it.displayName)
                        }, shape = CircleShape,
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = it.getPriorityColor().copy(alpha = 0.4F),
                            selectedContainerColor = it.getPriorityColor().copy(alpha = 0.6F)
                        ),
                        border = FilterChipDefaults.filterChipBorder(
                            enabled = true,
                            selected = priority != it.displayName,
                            borderWidth = 2.dp,
                            selectedBorderWidth = 2.dp
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                OutlinedButton(onClick = {
                    onCancel.invoke()
                }) {
                    ButtonText(text = "Cancel",
                        textColor = MaterialTheme.colorScheme.onBackground)
                }

                Button(onClick = {
                    onAddTask.invoke()
                }) {
                    ButtonText(text = "Save")

                }
            }

        }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Composable
fun TaskCard(
    modifier: Modifier = Modifier,
    title: String,
    tasks: List<Task>,
    backgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    onOptionSelected: (Task, Boolean) -> Unit = { i, b -> },
    onEdit: (Task) -> Unit = {},
    onDeleted: (Task) -> Unit = {}
) {

    Column(
        modifier = Modifier
            .padding(vertical = 8.dp)
            .border(
                2.dp,
                MaterialTheme.colorScheme.primary.copy(alpha = 0.8F),
                RoundedCornerShape(8.dp)
            )
    ) {
        SubtitleText(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = backgroundColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .padding(12.dp),
            text = title
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth().padding(8.dp),
        ) {
            items(tasks) { task ->
                SwipeMenuItem(
                    item = task.taskName,
                    isChecked = task.isCompleted,
                    onCheckedChange = {
                        onOptionSelected(task, it)
                    }, onDeleted = {
                        onDeleted(task)
                    },
                    onEdit = {
                        onEdit.invoke(task)
                    }
                )
            }
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun TaskCardPreview() {
    PersonalNoteTheme {
        TaskCard(
            title = "HIGH",
            tasks = listOf(Task(1, "Task 1", "HIGH", false), Task(2, "Task 2", "MEDIUM", false))
        )
    }

}