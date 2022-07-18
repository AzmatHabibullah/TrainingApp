package com.azmat.testdrivendevelopment.db.repo

import androidx.lifecycle.LiveData
import com.azmat.testdrivendevelopment.db.model.Category
import com.azmat.testdrivendevelopment.utils.Resource
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategoryNames() : Resource<Flow<List<String>>>
    suspend fun addCategory(categoryName: String): Resource<Long>
    suspend fun isNewCategoryNameValid(categoryName: String): Boolean
    suspend fun isExistingCategoryNameValid(categoryName: String): Boolean
}