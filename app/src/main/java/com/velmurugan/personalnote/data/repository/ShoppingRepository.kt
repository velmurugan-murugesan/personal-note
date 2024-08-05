package com.velmurugan.personalnote.data.repository

import com.velmurugan.personalnote.data.dao.ShoppingDao
import com.velmurugan.personalnote.data.entity.Shopping
import com.velmurugan.personalnote.data.model.TaskCount
import kotlinx.coroutines.flow.Flow

class ShoppingRepository(
    private val shoppingDao: ShoppingDao
) {
    fun getAllShoppingItems(): Flow<List<Shopping>> {
        return shoppingDao.getAllShopping()
    }

    suspend fun addShoppingItem(shopping: Shopping) {
        shoppingDao.createShopping(shopping)
    }

    suspend fun addMultipleShoppingItems(shopping: List<Shopping>) {
        shoppingDao.createShopping(shopping)
    }

    suspend fun updateShoppingItem(shopping: Shopping) {
        shoppingDao.updateShopping(shopping)
    }

    suspend fun deleteShoppingItem(shopping: Shopping) {
        shoppingDao.deleteShopping(shopping)
    }

    fun getTaskCount() : Flow<TaskCount> {
        return shoppingDao.getTaskCounts()
    }

    fun getShoppingList() = shoppingDao.getShoppingList()

    fun cleanShopping() {
        return shoppingDao.cleanShopping()
    }
}