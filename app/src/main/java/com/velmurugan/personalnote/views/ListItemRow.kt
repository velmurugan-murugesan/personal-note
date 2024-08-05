package com.velmurugan.personalnote.views

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.wear.compose.material.ContentAlpha
import com.velmurugan.personalnote.R

@Composable
fun ListItemRow(
    modifier: Modifier = Modifier,
    text: String,
    isChecked: Boolean,
    onEdit: () -> Unit = {},
    onDelete: () -> Unit = {},
    onCheckedChange: (Boolean) -> Unit = {}
) {


    val textColor = if (!isChecked) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.onSurface.copy(alpha = ContentAlpha.disabled)
    }

    Row(
        modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically) {
        Checkbox(checked = isChecked, onCheckedChange = {
            onCheckedChange.invoke(it)
        },
            colors = CheckboxDefaults.colors(
                checkedColor = MaterialTheme.colorScheme.primary.copy(alpha = ContentAlpha.disabled),
                uncheckedColor = MaterialTheme.colorScheme.onSurface
            ))
        BodyText(
            modifier = Modifier.weight(1f).clickable {
                onEdit.invoke()
            },
            text = text,
            color = textColor)
        Icon(imageVector = Icons.Default.Close, contentDescription = "delete",
            modifier = Modifier.clickable {
                onDelete.invoke()
            },
            tint = textColor)

    }

}


@Composable
fun ExpandableListItemView(
    modifier: Modifier = Modifier,
    text: String,
    onExpand: (Boolean) -> Unit = {},
    content: @Composable () -> Unit = {}
) {



    Column(
        Modifier
            .fillMaxWidth()) {
        Row(
            Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SubtitleText(text = text)
            /*Spacer(modifier = Modifier.width(16.dp))
            Icon(
                modifier = Modifier.size(32.dp),
                imageVector = expandIcon, contentDescription = "icon")*/
        }
        content()
    }

}

@Composable
fun EmptyListView(modifier: Modifier = Modifier,
                  text: String) {
    Column(
        modifier
            .fillMaxWidth()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Icon(
            modifier = Modifier.size(80.dp),
            painter = painterResource(id = R.drawable.empty), contentDescription = "empty")
        Spacer(modifier = Modifier.height(16.dp))
        BodyText(text = text)
    }
}

@Preview(showBackground = true)
@Composable
private fun EmptyListViewPreview() {
    EmptyListView(
        text = "No pending task"
    )
}

@Preview(showBackground = true)
@Composable
private fun ExpandableListViewPreview() {
    Column {

        ExpandableListItemView(
            text = "Today", content = {
                ListItemRow(
                    modifier = Modifier,
                    text = "Hello",
                    isChecked = false,
                )
                ListItemRow(
                    modifier = Modifier,
                    text = "Hello",
                    isChecked = false,
                )

                ListItemRow(
                    modifier = Modifier,
                    text = "Hello",
                    isChecked = true,
                )
            }
        )

        ExpandableListItemView(
            text = "Tomorrow", content = {
                ListItemRow(
                    modifier = Modifier,
                    text = "Hello",
                    isChecked = false,
                )
                ListItemRow(
                    modifier = Modifier,
                    text = "Hello",
                    isChecked = false,
                )

                ListItemRow(
                    modifier = Modifier,
                    text = "Hello",
                    isChecked = true,
                )
            }
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun ListItemRowPreview() {
    Column {
        ListItemRow(
            modifier = Modifier,
            text = "Hello",
            isChecked = false,
            onEdit = {

            },
            onCheckedChange = {

            }
        )

        ListItemRow(
            modifier = Modifier,
            text = "Hello",
            isChecked = false,
            onEdit = {})


        ListItemRow(
            modifier = Modifier,
            text = "Hello",
            isChecked = true,
            onEdit = {})

    }
}