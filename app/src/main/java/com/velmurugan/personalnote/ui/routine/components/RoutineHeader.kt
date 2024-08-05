package com.velmurugan.personalnote.ui.routine.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.velmurugan.personalnote.data.entity.Routine
import com.velmurugan.personalnote.utils.DateUtil
import com.velmurugan.personalnote.utils.DateUtil.getDay
import com.velmurugan.personalnote.views.BodyText
import com.velmurugan.personalnote.views.SubtitleText
import com.velmurugan.personalnote.views.UnderLineBodyText

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun RoutineHeader(
    modifier: Modifier = Modifier,
    header: String,
    items: List<DayCardItem>,
    selectedDay: String,
    onDayClick: (DayCardItem) -> Unit = {}
) {

    Card( Modifier
        .fillMaxWidth()) {
        Column(
            Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp, horizontal = 8.dp)
        ) {
            Row(
                Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SubtitleText(text = header)
                UnderLineBodyText(text = "View details")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                items.forEach {
                    DayCard(
                        modifier = Modifier
                            .weight(1F)
                            .clickable { onDayClick(it) },
                        day = it.date.getDay().toString(),
                        isCompleted = it.isCompleted,
                        isSelected = it.date == selectedDay
                    )
                }
            }

        }
    }


}


data class DayCardItem(
    val date: String,
    val day: String,
    val isCompleted: Boolean
)


@Preview(showBackground = true)
@Composable
private fun RoutineHeaderPreview() {
    RoutineHeader(
        header = "Routines", items = listOf(
            DayCardItem("12", "Mon",false),
            DayCardItem("13" ,"Tue",true),
            DayCardItem("14", "Wed",false),
            DayCardItem("15", "Thu",false),
            DayCardItem("16", "Fri",false),
            DayCardItem("17", "Sat",false),
            DayCardItem("18", "Sun",false),
            ),
        selectedDay = "18"
    )
}