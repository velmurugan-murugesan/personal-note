package com.velmurugan.personalnote.ui.shopping

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.velmurugan.personalnote.data.entity.Shopping
import com.velmurugan.personalnote.data.model.TaskCount
import com.velmurugan.personalnote.data.repository.ShoppingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingViewModel @Inject constructor(
    private val shoppingRepository: ShoppingRepository
) : ViewModel() {

    private val _shoppingUiState = MutableStateFlow(ShoppingUiState())
    val shoppingUiState = _shoppingUiState.asStateFlow()

    private val _shoppingEvent = MutableSharedFlow<ShoppingEvent>()
    val shoppingEvent = _shoppingEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            shoppingRepository.getAllShoppingItems().collect { routines ->
                _shoppingUiState.value =
                    _shoppingUiState.value.copy(shoppingItems = routines)
            }
        }

        viewModelScope.launch {
            shoppingRepository.getTaskCount().collect {
                _shoppingUiState.value =
                    _shoppingUiState.value.copy(taskCount = it)
            }
        }
    }

    fun updateShoppingItem(item: String) {
        _shoppingUiState.value = _shoppingUiState.value.copy(newShoppingItem = item)
    }

    fun createShoppingItem() {
        if (shoppingUiState.value.selectedShoppingItem == null) {
            viewModelScope.launch {
                runCatching {
                    if (shoppingUiState.value.newShoppingItem.isNotBlank()) {

                        val shoppingItems = shoppingUiState.value.newShoppingItem.split(",").map { it.trim() }.filter { it.isNotBlank() }
                        val shoppingList = shoppingItems.map {
                            Shopping(
                                itemName = it.trim(),
                                isPurchased = false
                            )
                        }
                        shoppingRepository.addMultipleShoppingItems(shoppingList)
                    }
                }.onSuccess {
                    _shoppingUiState.value = _shoppingUiState.value.copy(newShoppingItem = "")
                    _shoppingEvent.emit(ShoppingEvent.HideAddShoppingItemDialog)
                }.onFailure {
                    Log.d("Failed", it.message.orEmpty())
                }
            }
        } else {
            viewModelScope.launch {
                _shoppingUiState.value.selectedShoppingItem?.let {
                    shoppingRepository.updateShoppingItem(it.copy(itemName = shoppingUiState.value.newShoppingItem))
                }.also {
                    _shoppingUiState.value = _shoppingUiState.value.copy(newShoppingItem = "",
                        selectedShoppingItem = null)
                    _shoppingEvent.emit(ShoppingEvent.HideAddShoppingItemDialog)
                }
            }
        }
    }

    fun updateShoppingItem(shopping: Shopping, isCompleted: Boolean) {
        viewModelScope.launch {
            shoppingRepository.updateShoppingItem(shopping.copy(isPurchased = isCompleted))
        }
    }

    fun updateSelectedShoppingItem(shopping: Shopping) {
        _shoppingUiState.value = _shoppingUiState.value.copy(selectedShoppingItem = shopping,
            newShoppingItem = shopping.itemName)

    }

    fun deleteShoppingItem(shopping: Shopping) {
        viewModelScope.launch {
            shoppingRepository.deleteShoppingItem(shopping)
        }
    }


}

sealed class ShoppingEvent {
    object HideAddShoppingItemDialog : ShoppingEvent()
}

data class ShoppingUiState(
    val selectedShoppingItem: Shopping? = null,
    val shoppingItems: List<Shopping> = emptyList(),
    val newShoppingItem: String = "",
    val taskCount: TaskCount = TaskCount(0,0)
)