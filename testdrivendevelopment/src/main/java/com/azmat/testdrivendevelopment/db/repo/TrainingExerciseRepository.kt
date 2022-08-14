package com.azmat.testdrivendevelopment.db.repo

import com.azmat.testdrivendevelopment.db.model.TrainingExercise
import com.azmat.testdrivendevelopment.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TrainingExerciseRepository {
    suspend fun insert(trainingExercise: TrainingExercise): Resource<Long>
    suspend fun getExerciseUid(trainingExerciseUid: Long): Resource<Long>
    fun getInWorkout(workoutUid: Long) : Resource<Flow<List<TrainingExercise>>>
    suspend fun delete(trainingExerciseUid: Long)
    suspend fun countExercises(workoutUid: Long): Resource<Int>
    fun getTrainingExercises(): Resource<Flow<List<TrainingExercise>>>
}