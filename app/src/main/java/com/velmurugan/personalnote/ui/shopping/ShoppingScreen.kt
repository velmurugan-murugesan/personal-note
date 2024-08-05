package com.velmurugan.personalnote.ui.shopping

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.velmurugan.personalnote.data.entity.Shopping
import com.velmurugan.personalnote.ui.home.HorizontalProgress
import com.velmurugan.personalnote.views.AddItemContent
import com.velmurugan.personalnote.views.CheckableRow
import com.velmurugan.personalnote.views.FabButton
import com.velmurugan.personalnote.views.ListItemRow
import com.velmurugan.personalnote.views.SwipeMenuItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    shoppingViewModel: ShoppingViewModel = hiltViewModel()
) {

    val shoppingUiState by shoppingViewModel.shoppingUiState.collectAsState()

    var showAddShoppingItemDialog by remember {
        mutableStateOf(false)
    }
    val modalBottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true
    )

    if (showAddShoppingItemDialog) {
        ModalBottomSheet(
            onDismissRequest = {
                showAddShoppingItemDialog = false
            },
            sheetState = modalBottomSheetState
        ) {
            AddItemContent(
                title = "Add Shopping Item",
                label = "Shopping item",
                item = shoppingUiState.newShoppingItem,
                onItemUpdated = {
                    shoppingViewModel.updateShoppingItem(it)
                },
                onCancel = {
                    showAddShoppingItemDialog = false
                    shoppingViewModel.updateShoppingItem("")
                },
                onAdd = {
                    showAddShoppingItemDialog = false
                    shoppingViewModel.createShoppingItem()
                })
        }
    }


    Scaffold(
        floatingActionButton = {
            FabButton(text = "Add Item", onClick = {
                showAddShoppingItemDialog = true
            })
        }
    ) { padding ->
        Column(
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        ) {

            val progress = if (shoppingUiState.taskCount.totalCount > 0) {
                shoppingUiState.taskCount.completedCount.toFloat() / shoppingUiState.taskCount.totalCount.toFloat()
            } else 0F

            HorizontalProgress(
                title = "Shopping ${shoppingUiState.taskCount.completedCount} of ${shoppingUiState.taskCount.totalCount} completed ",
                progress = progress
            )
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier
                .fillMaxWidth()) {
                items(shoppingUiState.shoppingItems) {
                    ListItemRow(
                        text = it.itemName,
                        isChecked = it.isPurchased,
                        onCheckedChange = {isChecked ->
                            shoppingViewModel.updateShoppingItem(it, isChecked)
                        },
                        onDelete = {
                            shoppingViewModel.deleteShoppingItem(it)
                        },
                        onEdit = {
                            shoppingViewModel.updateSelectedShoppingItem(it)
                            showAddShoppingItemDialog = true
                        }
                    )
                }
            }
        }
    }

    LaunchedEffect(key1 = Unit) {
        shoppingViewModel.shoppingEvent.collect {
            when (it) {
                ShoppingEvent.HideAddShoppingItemDialog -> {
                    showAddShoppingItemDialog = false
                }
            }
        }
    }
}

