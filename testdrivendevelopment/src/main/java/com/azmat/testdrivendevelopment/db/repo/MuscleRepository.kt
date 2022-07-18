package com.azmat.testdrivendevelopment.db.repo

import com.azmat.testdrivendevelopment.utils.Resource
import kotlinx.coroutines.flow.Flow

interface MuscleRepository {
    fun getMuscleNames() : Resource<Flow<List<String>>>
    suspend fun addMuscle(muscleName: String): Resource<Long>
    suspend fun isNewMuscleNameValid(muscleName: String): Boolean
    suspend fun isExistingMuscleNameValid(muscleName: String): Boolean
}