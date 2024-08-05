package com.velmurugan.personalnote.ui.routine.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.velmurugan.personalnote.views.BodyText
import com.velmurugan.personalnote.views.CaptionText
import com.velmurugan.personalnote.views.SubtitleText


@Composable
fun DayCard(
    modifier: Modifier = Modifier,
    day: String,
    isCompleted: Boolean = false,
    isSelected: Boolean = false
) {

    val icon = if (isCompleted) {
        Icons.Default.CheckCircle
    } else {
        Icons.Default.Close

    }

    val background = if (isSelected) {
        MaterialTheme.colorScheme.primary
    } else {
        MaterialTheme.colorScheme.primaryContainer
    }

    val textColor = if (isSelected) {
        MaterialTheme.colorScheme.onPrimary
    } else  {
        MaterialTheme.colorScheme.onPrimaryContainer
    }

    Box(
        modifier = modifier
            .width(90.dp)
            .height(80.dp)
            .padding(4.dp)
            .background(background, RoundedCornerShape(16.dp))
            .border(
                width = 2.dp, color = MaterialTheme.colorScheme.primary,
                shape = RoundedCornerShape(16.dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (isCompleted) {
                Icon(
                    imageVector = icon, contentDescription = "",
                    modifier = Modifier.size(24.dp), tint =
                    textColor
                )
            } else {
                Canvas(
                    modifier = Modifier
                        .size(24.dp)
                        .padding(2.dp)
                ) {
                    val radius = size.minDimension / 2
                    drawCircle(
                        color = textColor,
                        radius = radius,
                        style = Stroke(
                            width = 1.dp.toPx(), // Set the stroke width
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }
            }


            Spacer(modifier = Modifier.height(4.dp))

            BodyText(
                modifier = Modifier,
                text = day,
                color = textColor
            )

        }


    }

}


@Preview(showBackground = true   )
@Composable
private fun DayCardPreview() {
    Row(Modifier.fillMaxWidth()) {
        DayCard(
            modifier = Modifier.weight(1F),
            day = "13",
            isCompleted = true,
            isSelected = true)
        DayCard(
            modifier = Modifier.weight(1F),
            day = "13",
            isCompleted = true,
            isSelected = false)
        DayCard(
            modifier = Modifier.weight(1F),
            day = "13",
            isCompleted = false,
            isSelected = true)
        DayCard(
            modifier = Modifier.weight(1F),
            day = "13",
            isCompleted = false,
            isSelected = false)
    }
}
