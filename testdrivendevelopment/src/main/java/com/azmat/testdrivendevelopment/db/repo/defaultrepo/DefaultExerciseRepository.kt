package com.azmat.testdrivendevelopment.db.repo.defaultrepo

import android.util.Log
import com.azmat.testdrivendevelopment.db.dao.CategoryDao
import com.azmat.testdrivendevelopment.db.dao.ExerciseDao
import com.azmat.testdrivendevelopment.db.dao.MuscleDao
import com.azmat.testdrivendevelopment.db.model.Exercise
import com.azmat.testdrivendevelopment.db.repo.ExerciseRepository
import com.azmat.testdrivendevelopment.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultExerciseRepository @Inject constructor(
    private val exerciseDao: ExerciseDao,
    private val categoryDao: CategoryDao,
    private val muscleDao: MuscleDao
) : ExerciseRepository {
    override fun getExerciseNames(): Resource<Flow<List<String>>> {
        return try {
            Resource.Success(exerciseDao.getExerciseNames())
        } catch (e: Exception) {
            Resource.Error("Failed to get exercise names from DAO")
        }
    }

    override fun getExercisesInCategory(category: String): Resource<Flow<List<String>>> {
        return try {
            Log.d("DER", "trying $category")
            Resource.Success(exerciseDao.getExercisesInCategory(category))
        } catch (e: Exception) {
            Resource.Error("Failed to get exercise names in category $category from DAO")
        }
    }

    override suspend fun getExerciseFromId(exerciseUid: Long): Resource<Exercise> {
        return try {
            Log.d("DER", "trying to get exercise $exerciseUid")
            Resource.Success(exerciseDao.getExerciseFromUid(exerciseUid))
        } catch (e: Exception) {
            Resource.Error("Failed to get exercise with id $exerciseUid")
        }
    }

    override fun getExercises(): Resource<Flow<List<Exercise>>> {
        return try {
            Resource.Success(exerciseDao.getExercises())
        } catch (e: Exception) {
            Resource.Error("Failed to get exercises from DAO")
        }
    }

    override suspend fun insert(exerciseName: String, categoryName: String, primaryMuscleName: String,
    secondaryMuscleName: String): Resource<Long> {
        return try {
            if (isNewExerciseNameValid(exerciseName)) {
                val newExercise = Exercise(
                    0,
                    exerciseName,
                    categoryDao.getCategoryByName(categoryName),
                    muscleDao.getMuscleByName(primaryMuscleName),
                    muscleDao.getMuscleByName(secondaryMuscleName)
                )
                Resource.Success(exerciseDao.insert(newExercise))
            } else {
                Resource.Error("Cannot enter duplicate exercise name")
            }
        } catch (e: Exception) {
            Resource.Error("Could not add exercise $exerciseName")
        }
    }

    override suspend fun isNewExerciseNameValid(exerciseName: String): Boolean {
        return exerciseDao.countExerciseName(exerciseName) == 0
    }

}