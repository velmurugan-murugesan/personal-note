package com.velmurugan.personalnote.views

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.velmurugan.personalnote.base.PRIORITY_HIGH_COLOR
import com.velmurugan.personalnote.base.PRIORITY_LOW_COLOR
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun SwipeMenuItem(
    modifier: Modifier = Modifier,
    item: String,
    isEnabled: Boolean = true,
    isChecked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDeleted: () -> Unit
) {
    val density = LocalDensity.current
    val defaultActionSize = 70.dp
    val endActionSizePx = with(density) { (defaultActionSize * 2).toPx() }

    val coroutineScope = rememberCoroutineScope()

    var shape by remember {
        mutableStateOf(RoundedCornerShape(16.dp))
    }

    val state = remember {
        AnchoredDraggableState(
            initialValue = DragAnchors.Center,
            anchors = DraggableAnchors {
                DragAnchors.Center at 0f
                DragAnchors.End at endActionSizePx
            },
            confirmValueChange = {
                when (it) {
                    DragAnchors.End -> {
                        shape = RoundedCornerShape(topEnd = 0.dp, bottomEnd = 0.dp)
                        true
                    }

                    DragAnchors.Center -> {
                        shape = RoundedCornerShape(16.dp)
                        true
                    }

                    else -> {
                        false
                    }
                }
            },
            positionalThreshold = { distance: Float -> distance * 0.5f },
            velocityThreshold = { with(density) { 100.dp.toPx() } },
            animationSpec = tween(),
        )
    }

    val backgroundColor = if (isEnabled) {
        MaterialTheme.colorScheme.primaryContainer
    } else {
        Color.LightGray
    }

    LaunchedEffect(isEnabled) {
        state.animateTo(DragAnchors.Center)
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RectangleShape)
    ) {

        Row(
            modifier = Modifier
                .height(defaultActionSize)
                .align(Alignment.CenterEnd)
                .clip(RoundedCornerShape(topEnd = 16.dp, bottomEnd = 16.dp))
                .offset {
                    IntOffset(
                        x = -state
                            .requireOffset()
                            .roundToInt() + endActionSizePx.roundToInt(),
                        y = 0,
                    )
                },
            verticalAlignment = Alignment.CenterVertically,
        ) {

            ActionItem(
                modifier = Modifier.size(defaultActionSize),
                text = "Edit",
                icon = Icons.Default.Edit,
                backgroundColor = PRIORITY_LOW_COLOR,
                onClick = {
                    coroutineScope.launch {
                        state.animateTo(DragAnchors.Center)
                    }
                    onEdit.invoke()
                })

            ActionItem(
                modifier = Modifier.size(defaultActionSize),
                text = "Delete",
                icon = Icons.Default.Delete,
                backgroundColor = PRIORITY_HIGH_COLOR,
                onClick = {
                    coroutineScope.launch {
                        state.animateTo(DragAnchors.Center)
                    }
                    onDeleted.invoke()
                })

        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.CenterStart)
                .offset {
                    IntOffset(
                        x = -state
                            .requireOffset()
                            .roundToInt(),
                        y = 0,
                    )
                }
                .anchoredDraggable(
                    enabled = isEnabled,
                    state = state,
                    orientation = Orientation.Horizontal,
                    reverseDirection = true
                ),

            ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .background(
                        backgroundColor,
                        shape = shape
                    )
                    .padding(horizontal = 8.dp)
                    .height(defaultActionSize)
                    .clickable {
                        if (isEnabled) {
                            onCheckedChange.invoke(!isChecked)
                            coroutineScope.launch {
                                state.animateTo(DragAnchors.Center)
                            }
                        }
                    },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                BodyText(text = item)
                Checkbox(
                    checked = isChecked,
                    enabled = isEnabled,
                    onCheckedChange = {
                        coroutineScope.launch {
                            state.animateTo(DragAnchors.Center)
                        }
                        onCheckedChange.invoke(it)
                    }
                )
            }
        }
    }

}

@Composable
fun ActionItem(
    modifier: Modifier = Modifier, text: String,
    icon: ImageVector,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = modifier
            .background(backgroundColor)
            .clickable {
                onClick.invoke()
            },
        contentAlignment = Alignment.Center
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                modifier = Modifier
                    .size(22.dp),
                imageVector = icon,
                contentDescription = null,
                tint = Color.White
            )

            CaptionText(
                text = text,
                textColor = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}


@Composable
fun Dp.toPx(): Float {
    val density = LocalDensity.current
    return with(density) { this.density * density.density }
}

@Preview(showBackground = true)
@Composable
private fun SwipeMenuCheckableRowPreview() {

    SwipeMenuItem(
        modifier = Modifier,
        item = "Hello",
        isChecked = false,
        onCheckedChange = {

        },
        onEdit = {
            /*TODO*/
        },
        onDeleted = {
        },

        )
}