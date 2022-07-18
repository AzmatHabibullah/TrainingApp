package com.azmat.testdrivendevelopment.db.repo

import com.azmat.testdrivendevelopment.db.model.TrainingWorkout
import com.azmat.testdrivendevelopment.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TrainingWorkoutRepository {
    suspend fun insert(workout: TrainingWorkout): Resource<Long>
    suspend fun delete(workoutUid: Long)
    fun getWorkoutsByDate(date: Long): Resource<Flow<List<TrainingWorkout>>>
    suspend fun getWorkoutDateById(workoutUid: Long): Resource<Long>
}