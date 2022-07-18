package com.azmat.testdrivendevelopment.db.repo.defaultrepo

import android.util.Log
import com.azmat.testdrivendevelopment.db.dao.CategoryDao
import com.azmat.testdrivendevelopment.db.model.Category
import com.azmat.testdrivendevelopment.db.repo.CategoryRepository
import com.azmat.testdrivendevelopment.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultCategoryRepository @Inject constructor(
    private val categoryDao: CategoryDao
) : CategoryRepository {

    override fun getCategoryNames(): Resource<Flow<List<String>>> {
        Log.d("DCR", "getting a category")
        return try {
            val names = categoryDao.getCategoryNames()
            Resource.Success(names)
        } catch (e: Exception) {
            Log.d("DCR", "failed")
            Resource.Error("Failed to get category names from DAO")
        }
    }

    override suspend fun addCategory(categoryName: String): Resource<Long> {
        Log.d("DCR", "adding a category")
        return try {
            if (isNewCategoryNameValid(categoryName)) {
                Log.d("DCR", "here")
                Resource.Success(categoryDao.insert(Category(0, categoryName)))
            } else {
                Resource.Error("Cannot enter duplicate category")
            }
        } catch (e: Exception) {
            Log.d("DCR", "failed $e")
            Resource.Error("Failed to insert category $categoryName")
        }
    }

    override suspend fun isNewCategoryNameValid(categoryName: String) : Boolean {
        return categoryDao.countCategoryName(categoryName) == 0
    }

    override suspend fun isExistingCategoryNameValid(categoryName: String) : Boolean {
        return categoryDao.countCategoryName(categoryName) == 1
    }

}