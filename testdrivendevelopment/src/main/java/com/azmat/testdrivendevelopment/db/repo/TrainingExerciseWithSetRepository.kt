package com.azmat.testdrivendevelopment.db.repo

import com.azmat.testdrivendevelopment.db.model.TrainingExercise
import com.azmat.testdrivendevelopment.db.model.TrainingExerciseWithSet
import com.azmat.testdrivendevelopment.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TrainingExerciseWithSetRepository {
    fun getAllData(): Resource<Flow<List<TrainingExerciseWithSet>>>
}