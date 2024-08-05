package com.velmurugan.personalnote.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.velmurugan.personalnote.data.entity.Task



@Composable
fun TaskCheckboxGroup(
    modifier: Modifier = Modifier,
    title: String = "Header 1",
    options: List<Task> = emptyList(),
    onOptionSelected: (Task, Boolean) -> Unit = {i, b -> },
    onDeleted: (Task) -> Unit = {}
) {
    var isExpanded by remember {
        mutableStateOf(true)
    }

    val expandIcon = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown


    Row(
        Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                isExpanded = !isExpanded
            }, verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween) {
        SubtitleText(text = title)
        Icon(imageVector = expandIcon, contentDescription = "")
    }
    AnimatedVisibility(visible = isExpanded) {
        LazyColumn {
            items(options) { option ->
                CheckableRow(
                    text = option.taskName,
                    isChecked = option.isCompleted,
                    onCheckedChange = {
                        onOptionSelected(option, it)
                    }, onDeleted = {
                        onDeleted(option)
                    }
                )
            }
        }




    }



}

@Preview
@Composable
fun CheckboxGroupPreview() {
    Column {
        TaskCheckboxGroup(
            modifier = Modifier,
            title = "Header 1",
            options = listOf(),
            onOptionSelected = {i, o -> },
            onDeleted = {}
        )
    }

}