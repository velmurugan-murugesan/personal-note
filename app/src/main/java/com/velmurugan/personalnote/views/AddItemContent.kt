package com.velmurugan.personalnote.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp

@Composable
fun AddItemContent(
    modifier: Modifier = Modifier,
    title: String,
    label: String,
    item: String,
    onItemUpdated: (String) -> Unit = {},
    onAdd: () -> Unit = {},
    onCancel: () -> Unit = {},
) {
    val focusRequester = remember { FocusRequester() }

    var textFieldValue by remember {
        mutableStateOf(
            TextFieldValue(
                text = item,
                selection = TextRange(item.length)
            )
        )
    }


    Column(
                modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {

              /*  SubtitleText(text = title)
                Spacer(modifier = Modifier.height(8.dp))*/
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    label = {
                        BodyText(text = label)
                    },
                    value = textFieldValue, onValueChange = { newValue ->
                        textFieldValue = newValue.copy(
                            selection = TextRange(newValue.text.length)
                        )
                        onItemUpdated.invoke(newValue.text)
                    },
                    minLines = 3,
                    maxLines = 5
                )
                Spacer(modifier = Modifier.height(8.dp))
                
                CaptionText(text = "To add multiple routines, separate each with a comma (,)")

                Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {

                    OutlinedButton(onClick = {
                        onCancel.invoke()
                    }) {
                        ButtonText(text = "Cancel", textColor = MaterialTheme.colorScheme.onBackground)
                    }
                    Button(modifier = Modifier.width(100.dp), onClick = {
                        onAdd.invoke()
                    }) {
                        ButtonText(text = "Add")
                    }
                }
            }

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }

}

