package com.azmat.testdrivendevelopment.db.repo.defaultrepo

import com.azmat.testdrivendevelopment.db.dao.TrainingExerciseDao
import com.azmat.testdrivendevelopment.db.model.TrainingExercise
import com.azmat.testdrivendevelopment.db.repo.TrainingExerciseRepository
import com.azmat.testdrivendevelopment.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultTrainingExerciseRepository @Inject constructor(
    private val trainingExerciseDao: TrainingExerciseDao
) : TrainingExerciseRepository {

    override suspend fun insert(trainingExercise: TrainingExercise): Resource<Long> {
        return try {
            Resource.Success(trainingExerciseDao.insert(trainingExercise))
        } catch (e: Exception) {
            Resource.Error("Could not insert $trainingExercise")
        }
    }

    override suspend fun getExerciseUid(trainingExerciseUid: Long): Resource<Long> {
       return try {
           Resource.Success(trainingExerciseDao.getExerciseUid(trainingExerciseUid))
       } catch (e: Exception) {
           Resource.Error("Could not get exercise for $trainingExerciseUid")
       }
    }

    override fun getInWorkout(workoutUid: Long): Resource<Flow<List<TrainingExercise>>> {
        return try {
            Resource.Success(trainingExerciseDao.getInWorkout(workoutUid))
        } catch (e: Exception) {
            Resource.Error("Could not get exercises from workout $workoutUid")
        }
    }

    override suspend fun delete(trainingExerciseUid: Long) {
        trainingExerciseDao.delete(trainingExerciseUid)
    }

    override suspend fun countExercises(workoutUid: Long): Resource<Int> {
        TODO("Not yet implemented")
    }
}