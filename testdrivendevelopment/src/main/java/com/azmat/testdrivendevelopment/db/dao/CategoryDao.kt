package com.azmat.testdrivendevelopment.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.azmat.testdrivendevelopment.db.model.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {
    @Insert
    suspend fun insert(category: Category): Long

    @Insert
    suspend fun insert(vararg categories: Category): List<Long>

    @Query("SELECT category_name FROM category_table")
    fun getCategoryNames() : Flow<List<String>>

    @Query("SELECT count(*) FROM category_table WHERE category_name=:categoryNameToCheck")
    fun countCategoryName(categoryNameToCheck: String): Int

    @Query("SELECT * FROM category_table WHERE category_name=:categoryName")
    fun getCategoryByName(categoryName: String) : Category

    @Delete
    suspend fun delete(category: Category)

    @Query("DELETE from category_table")
    suspend fun deleteAll()
}