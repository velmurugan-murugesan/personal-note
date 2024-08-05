package com.velmurugan.personalnote.ui.routine

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.velmurugan.personalnote.ui.home.HorizontalProgress
import com.velmurugan.personalnote.ui.routine.components.DayCardItem
import com.velmurugan.personalnote.ui.routine.components.RoutineHeader
import com.velmurugan.personalnote.utils.DateUtil
import com.velmurugan.personalnote.utils.DateUtil.getDay
import com.velmurugan.personalnote.views.AddItemContent
import com.velmurugan.personalnote.views.BodyText
import com.velmurugan.personalnote.views.ButtonText
import com.velmurugan.personalnote.views.CaptionText
import com.velmurugan.personalnote.views.EmptyListView
import com.velmurugan.personalnote.views.ExpandableListItemView
import com.velmurugan.personalnote.views.FabButton
import com.velmurugan.personalnote.views.ListItemRow
import com.velmurugan.personalnote.views.SubtitleText
import com.velmurugan.personalnote.views.SwipeMenuItem
import com.velmurugan.personalnote.views.UnderLineBodyText

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RoutineScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    routineViewModel: RoutineViewModel = hiltViewModel()
) {

    var showAddRoutineDialog by remember {
        mutableStateOf(false)
    }

    val routineUiState by routineViewModel.routineUiState.collectAsState()

    val modalBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    val pendingRoutines = remember {
        derivedStateOf {
            routineUiState.selectedDateRoutines.filter { !it.isCompleted }
        }
    }

    val completedRoutines = remember {
        derivedStateOf {
            routineUiState.selectedDateRoutines.filter { it.isCompleted }
        }

    }


    Scaffold(
        floatingActionButton = {
            AnimatedVisibility(routineUiState.isShowAddRoutine) {
                FabButton(text = "Add Routine", onClick = {
                    routineViewModel.updateRoutedName("")
                    showAddRoutineDialog = true
                })
            }
        }
    ) { padding ->

        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {


            if (showAddRoutineDialog) {

                ModalBottomSheet(
                    onDismissRequest = {
                        showAddRoutineDialog = false
                    },
                    sheetState = modalBottomSheetState
                ) {
                    AddItemContent(
                        title = "Add Routine",
                        label = "Routine",
                        item = routineUiState.newRouteName,
                        onItemUpdated = {
                            routineViewModel.updateRoutedName(it)
                        },
                        onCancel = {
                            showAddRoutineDialog = false
                        },
                        onAdd = {
                            showAddRoutineDialog = false
                            routineViewModel.addRoutine()
                        })

                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            RoutineDays(
                selectedDay = routineUiState.selectedRoutineDay,
                onDayClick = {
                    routineViewModel.updateSelectedDay(it)
                }
            )
            /* RoutineHeader(header = "Routine",
                 items = routineUiState.last7DaysRoutines,
                 selectedDay = routineUiState.selectedRoutineDay,
                 onDayClick = {
                     routineViewModel.updateSelectedDay(it.date)
                 })*/
            Spacer(modifier = Modifier.height(24.dp))


            //  BodyText(text = "Routine ${routineUiState.taskCount.completedCount} of ${routineUiState.taskCount.totalCount} completed ")
            //   Spacer(modifier = Modifier.height(8.dp))
            AnimatedVisibility(visible = pendingRoutines.value.isNotEmpty()) {
                ExpandableListItemView(text = "Pending") {
                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(pendingRoutines.value) { routine ->
                            ListItemRow(
                                text = routine.routineName,
                                isChecked = routine.isCompleted,
                                onDelete = {
                                    routineViewModel.deleteRoutine(routine)
                                },
                                onEdit = {
                                    routineViewModel.updateSelectedRoutine(routine)
                                    showAddRoutineDialog = true
                                },
                                onCheckedChange = {
                                    routineViewModel.updateRouteIsCompleted(routine, it)
                                })
                            //Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            AnimatedVisibility(visible = completedRoutines.value.isNotEmpty()) {
                ExpandableListItemView(text = "Completed") {
                    /* AnimatedVisibility(visible = routineUiState.completedRoutines.isEmpty()) {
                         EmptyListView(text = "No completed routines")
                     }*/

                    LazyColumn(modifier = Modifier.fillMaxWidth()) {
                        items(completedRoutines.value) { routine ->
                            ListItemRow(
                                text = routine.routineName,
                                isChecked = routine.isCompleted,
                                onDelete = {
                                    routineViewModel.deleteRoutine(routine)
                                },
                                onEdit = {
                                    routineViewModel.updateSelectedRoutine(routine)
                                    showAddRoutineDialog = true
                                },
                                onCheckedChange = {
                                    routineViewModel.updateRouteIsCompleted(routine, it)
                                })
                            // Spacer(modifier = Modifier.height(16.dp))
                        }
                    }
                }

            }

        }
    }

}


@Composable
fun RoutineDays(
    modifier: Modifier = Modifier,
    selectedDay: String,
    onDayClick: (String) -> Unit = {}
) {
    Row(
        Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val items = DateUtil.getLast7DaysWithDay()
        items.forEach {
            val backgroundColor = if (it.second == selectedDay) {
                MaterialTheme.colorScheme.primary
            } else {
                MaterialTheme.colorScheme.primary.copy(
                    alpha = 0.1f
                )
            }

            val textColor = if (it.second == selectedDay) {
                MaterialTheme.colorScheme.onPrimary
            } else {
                MaterialTheme.colorScheme.onBackground
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 4.dp)
                    .clip(
                        RoundedCornerShape(8.dp)
                    )
                    .clickable {
                        onDayClick(it.second)
                    }
                    .background(backgroundColor)
                    .padding(8.dp)

                    ,
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CaptionText(text = it.first, textColor = textColor)
                Spacer(modifier = Modifier.height(4.dp))
                CaptionText(text = it.second.getDay().toString(),
                    textColor = textColor)
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
private fun RoutineDaysPreview() {
    RoutineDays(
        selectedDay = "18"
    )

}