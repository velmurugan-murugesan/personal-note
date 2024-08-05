package com.velmurugan.personalnote.ui.weight

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.velmurugan.personalnote.views.FabButton

@Composable
fun WeightScreen(modifier: Modifier = Modifier, navController: NavController) {

    Scaffold(
        floatingActionButton = {
            FabButton(text = "Add Weight", onClick = {

            })
        }
    ) { padding ->
        Column(Modifier.fillMaxWidth()) {

        }
    }

}