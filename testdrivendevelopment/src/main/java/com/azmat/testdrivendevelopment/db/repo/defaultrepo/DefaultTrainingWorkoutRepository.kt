package com.azmat.testdrivendevelopment.db.repo.defaultrepo

import com.azmat.testdrivendevelopment.db.dao.TrainingWorkoutDao
import com.azmat.testdrivendevelopment.db.model.TrainingWorkout
import com.azmat.testdrivendevelopment.db.repo.TrainingWorkoutRepository
import com.azmat.testdrivendevelopment.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultTrainingWorkoutRepository @Inject constructor(
    private val trainingWorkoutDao: TrainingWorkoutDao
) : TrainingWorkoutRepository {

    override suspend fun insert(workout: TrainingWorkout): Resource<Long> {
        return try {
            Resource.Success(trainingWorkoutDao.insert(workout))
        } catch (e: Exception) {
            Resource.Error("Could not add training workout $workout")
        }
    }

    override suspend fun delete(workoutUid: Long) {
        trainingWorkoutDao.delete(workoutUid)
    }

    override fun getWorkoutsByDate(date: Long): Resource<Flow<List<TrainingWorkout>>> {
        return try {
            Resource.Success(trainingWorkoutDao.loadAllByDate(date))
        } catch (e: Exception) {
            Resource.Error("Could not get training workouts on dates $date")
        }
    }

    override suspend fun getWorkoutDateById(workoutUid: Long): Resource<Long> {
        return try {
            Resource.Success(trainingWorkoutDao.getDate(workoutUid))
        } catch (e: Exception) {
            Resource.Error("Could not get date for training workout $workoutUid")
        }
    }
}