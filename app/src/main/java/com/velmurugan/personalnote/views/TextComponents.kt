package com.velmurugan.personalnote.views

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun BodyText(modifier: Modifier = Modifier, text: String,
             color: Color = MaterialTheme.colorScheme.onBackground) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = color
    )
}

@Composable
fun UnderLineBodyText(modifier: Modifier = Modifier, text: String,
             textColor: Color = MaterialTheme.colorScheme.onBackground) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = textColor,
        textDecoration = TextDecoration.Underline
    )
}


@Composable
fun TitleText(modifier: Modifier = Modifier, text: String,
              textColor: Color = MaterialTheme.colorScheme.onBackground) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.headlineSmall,
        color = textColor
    )
}

@Composable
fun SubtitleText(modifier: Modifier = Modifier, text: String,
                 color: Color = MaterialTheme.colorScheme.onBackground) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.titleMedium,
        color = color
    )
}

@Composable
fun ButtonText(modifier: Modifier = Modifier, text: String,
               textColor: Color = MaterialTheme.colorScheme.onPrimary) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.bodyMedium,
        color = textColor
    )
}

@Composable
fun CaptionText(modifier: Modifier = Modifier, text: String,
                textColor: Color = MaterialTheme.colorScheme.onBackground) {
    Text(
        modifier = modifier,
        text = text,
        style = MaterialTheme.typography.labelSmall,
        color = textColor
    )
}




@Preview(showBackground = true)
@Composable
private fun TextComponentPreview() {
    Column {
        BodyText(text = "Body Textview")
        Spacer(modifier = Modifier.height(16.dp))

    }
}