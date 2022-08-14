package com.azmat.testdrivendevelopment.db.repo

import com.azmat.testdrivendevelopment.db.model.TrainingSet
import com.azmat.testdrivendevelopment.utils.Resource
import kotlinx.coroutines.flow.Flow

interface TrainingSetRepository {
    suspend fun insert(trainingSet: TrainingSet): Resource<Long>
    fun getSets(): Resource<Flow<List<TrainingSet>>>
    suspend fun getSetNumber(trainingSetUid: Long): Resource<Int>
    fun getSetsFromExercise(trainingExerciseUid: Long): Resource<Flow<List<TrainingSet>>>
    suspend fun getComment(trainingSetUid: Long): Resource<String>
    suspend fun updateComment(trainingSetUid: Long, comment: String)
    suspend fun updateVideoPath(trainingSetUid: Long, videoPath: String) // todo partial entity update
    suspend fun getVideoPath(trainingSetUid: Long): Resource<String>
    suspend fun delete(trainingExerciseUid: Long)
    suspend fun countExercises(workoutUid: Long): Resource<Int> // todo
}