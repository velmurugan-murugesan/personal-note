package com.velmurugan.personalnote.ui.home

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.velmurugan.personalnote.base.ROUTINE_COLOR
import com.velmurugan.personalnote.base.SHOPPING_COLOR
import com.velmurugan.personalnote.base.TASK_COLOR
import com.velmurugan.personalnote.base.WEIGHT_COLOR
import com.velmurugan.personalnote.data.model.TaskCount
import com.velmurugan.personalnote.ui.main.ROUTINE
import com.velmurugan.personalnote.ui.main.SHOPPING
import com.velmurugan.personalnote.ui.main.TASK
import com.velmurugan.personalnote.ui.main.WEIGHT
import com.velmurugan.personalnote.views.BodyText
import com.velmurugan.personalnote.views.CaptionText
import com.velmurugan.personalnote.views.SubtitleText

@Composable
fun HomeScreen(
    navController: NavController,
    onFabClick: () -> Unit = {},
    homeViewModel: HomeViewModel = hiltViewModel()
) {

    val homeUiState by homeViewModel.homeUiState.collectAsState()

    Column(
        Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {

        HomeItemCard(item = "Routines", count = homeUiState.routinesCount,
            isTaskCompleted = homeUiState.isRoutineCompleted,
            onClick = {
                navController.navigate(ROUTINE)
            },
            cardBackgroundColor = ROUTINE_COLOR)

        HomeItemCard(item = "Task List", count = homeUiState.tasksCount,
            isTaskCompleted = homeUiState.isTaskCompleted,
            onClick = {
                navController.navigate(TASK)
            },
            cardBackgroundColor = TASK_COLOR)

        HomeItemCard(item = "Shopping List", count = homeUiState.shoppingCount,
            isTaskCompleted = homeUiState.isShoppingCompleted,
            onClick = {
                navController.navigate(SHOPPING)
            },
            cardBackgroundColor = SHOPPING_COLOR)

        /*HomeItemCard(item = "Weight Monitor", count = homeUiState.shoppingCount,
            isTaskCompleted = homeUiState.isShoppingCompleted,
            onClick = {
                navController.navigate(WEIGHT)
            },
            cardBackgroundColor = WEIGHT_COLOR)*/
    }
}

@Preview
@Composable
fun HomeItemCard(
    modifier: Modifier = Modifier,
    cardBackgroundColor: Color = MaterialTheme.colorScheme.primaryContainer,
    item: String = "Items", count: TaskCount = TaskCount(4, 2),
    isTaskCompleted: Boolean = false,
    onClick: () -> Unit = {}
) {

    val backgroundColor = if (isTaskCompleted) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.onBackground.copy(
            alpha = 0.1f
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Card(modifier = modifier
            .fillMaxWidth()
            .clickable {
                onClick()
            }) {

            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(
                    Modifier
                        .weight(1f)
                ) {
                    SubtitleText(text = item, color = MaterialTheme.colorScheme.primary)

                    CaptionText(text = "Completed ${count.completedCount} of ${count.totalCount} ")
                }

                val progress = if (count.totalCount > 0) {
                    count.completedCount.toFloat() / count.totalCount.toFloat()
                } else 0F

                CircularProgressIndicator(
                    strokeWidth = 8.dp,
                    strokeCap = StrokeCap.Round,
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.inversePrimary,
                    progress = { progress },
                    modifier = Modifier
                        .size(60.dp)
                        .align(Alignment.CenterVertically),
                )
            }


        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}


@Preview(showBackground = true)
@Composable
fun HorizontalProgress(
    modifier: Modifier = Modifier,
    title: String = "Progress",
    progress: Float = 0.5F
) {
    Card(modifier = modifier.fillMaxWidth()) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.tertiaryContainer)
                .padding(12.dp)
        ) {
            CaptionText(text = title, textColor = MaterialTheme.colorScheme.onTertiaryContainer)
            Spacer(modifier = Modifier.height(12.dp))
            LinearProgressIndicator(
                progress = { progress },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(12.dp),
                color = MaterialTheme.colorScheme.primary,
                trackColor = MaterialTheme.colorScheme.inversePrimary,
                strokeCap = StrokeCap.Round
            )
        }
    }

}

