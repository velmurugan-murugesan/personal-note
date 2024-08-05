package com.velmurugan.personalnote.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.velmurugan.personalnote.data.entity.Shopping
import com.velmurugan.personalnote.data.model.TaskCount
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingDao {

    @Query("SELECT * FROM Shopping")
    fun getAllShopping(): Flow<List<Shopping>>

    @Query("SELECT * FROM Shopping")
    fun getShoppingList(): List<Shopping>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createShopping(shopping: Shopping)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun createShopping(shopping: List<Shopping>)

    @Delete
    fun deleteShopping(shopping: Shopping)

    @Update
    fun updateShopping(shopping: Shopping)

    @Query("SELECT COUNT(*) AS totalCount, SUM(CASE WHEN isPurchased THEN 1 ELSE 0 END) AS completedCount FROM Shopping")
    fun getTaskCounts(): Flow<TaskCount>

    @Query("DELETE FROM Shopping")
    fun cleanShopping()

}