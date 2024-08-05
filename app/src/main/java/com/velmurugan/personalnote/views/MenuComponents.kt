package com.velmurugan.personalnote.views

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun PopupMenuItems(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    menuItems: List<String>,
    onMenuAction: (String) -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = { onDismissRequest.invoke() }
    ) {
        menuItems.forEach { item ->
            DropdownMenuItem(
                text = { BodyText(text = item) },
                onClick = {
                    onMenuAction.invoke(item)
                }
            )
        }
    }
}