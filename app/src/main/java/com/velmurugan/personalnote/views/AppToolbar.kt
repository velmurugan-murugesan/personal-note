package com.velmurugan.personalnote.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun AppToolbar(
    modifier: Modifier = Modifier,
    title: String,
    count: String,
    onBack: () -> Unit = {},
) {

    Column(modifier.fillMaxWidth()) {
        Row(Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                onBack.invoke()
            }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(end = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                TitleText(text = title)
                TitleText(text = count)
            }

        }
        Spacer(modifier = Modifier.height(8.dp))
        HorizontalDivider()
    }

}

@Preview(showBackground = true)
@Composable
private fun AppToolbarPreview() {
    AppToolbar(title = "Hello", count = "1/2")
}