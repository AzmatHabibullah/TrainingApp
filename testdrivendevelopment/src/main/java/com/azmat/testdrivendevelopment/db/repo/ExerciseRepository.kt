package com.azmat.testdrivendevelopment.db.repo

import com.azmat.testdrivendevelopment.db.dao.ExerciseDao
import com.azmat.testdrivendevelopment.db.model.Exercise
import com.azmat.testdrivendevelopment.utils.Resource
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    fun getExerciseNames(): Resource<Flow<List<String>>>
    fun getExercises(): Resource<Flow<List<Exercise>>>
    fun getExercisesInCategory(category: String): Resource<Flow<List<String>>>
    suspend fun getExerciseFromId(exerciseUid: Long): Resource<Exercise>
    suspend fun insert(exerciseName: String, categoryName: String,
                       primaryMuscleName: String, secondaryMuscleName: String): Resource<Long>
    suspend fun isNewExerciseNameValid(exerciseName: String): Boolean
}