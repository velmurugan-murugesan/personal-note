package com.velmurugan.personalnote.views

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.velmurugan.personalnote.base.PRIORITY_HIGH_COLOR
import com.velmurugan.personalnote.base.PRIORITY_LOW_COLOR
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CheckableRow(
    modifier: Modifier = Modifier,
    text: String,
    isChecked: Boolean,
    onDeleted: () -> Unit = {},
    onCheckedChange: (Boolean) -> Unit = {}
) {

    val backgroundColor = if (isChecked) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1F)
    }
    Column {
        Row(
            modifier = modifier
                .padding(vertical = 8.dp)
                .background(
                    backgroundColor,
                    shape = RoundedCornerShape(16.dp)
                )
                .clip(RoundedCornerShape(16.dp)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = modifier
                    .weight(0.8F)
                    .combinedClickable(
                        onClick = {
                            onCheckedChange(!isChecked)
                        },

                        onLongClick = {

                        }
                    )
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                BodyText(text = text)
                Checkbox(checked = isChecked, onCheckedChange = onCheckedChange)
            }

            Icon(
                modifier = Modifier
                    .padding(8.dp)
                    .clickable {
                        onDeleted()
                    },
                imageVector = Icons.Default.Delete, contentDescription = "delete",
                tint = MaterialTheme.colorScheme.error
            )
        }

        // HorizontalDivider()
    }
}

enum class DragAnchors {
    Start,
    Center,
    End,
}


@Preview
@Composable
private fun SwipeRowPreview() {
    Surface {
        SwipeMenuItem(
            modifier = Modifier,
            item = "Hello",
            isChecked = false,
            onCheckedChange = {},
            onEdit = {

            },
            onDeleted = {

            }
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun CheckableRowPreview() {
    Column(Modifier.padding(8.dp)) {
        CheckableRow(
            modifier = Modifier,
            text = "Hello",
            isChecked = false,
            onDeleted = {},
            onCheckedChange = {}
        )

        CheckableRow(
            modifier = Modifier,
            text = "Hello",
            isChecked = true,
            onDeleted = {},
            onCheckedChange = {}
        )
    }
}