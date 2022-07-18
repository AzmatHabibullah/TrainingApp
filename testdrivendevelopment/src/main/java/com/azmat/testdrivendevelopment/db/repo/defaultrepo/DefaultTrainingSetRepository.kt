package com.azmat.testdrivendevelopment.db.repo.defaultrepo

import com.azmat.testdrivendevelopment.db.dao.TrainingSetDao
import com.azmat.testdrivendevelopment.db.model.TrainingSet
import com.azmat.testdrivendevelopment.db.repo.TrainingSetRepository
import com.azmat.testdrivendevelopment.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultTrainingSetRepository @Inject constructor(
    private val trainingSetDao: TrainingSetDao
): TrainingSetRepository {

    override suspend fun insert(trainingSet: TrainingSet): Resource<Long> {
        return try {
            Resource.Success(trainingSetDao.insert(trainingSet))
        } catch (e: Exception) {
            Resource.Error("Could not add training set $trainingSet")
        }
    }

    override suspend fun getSetNumber(trainingSetUid: Long): Resource<Int> {
        return try {
            Resource.Success(trainingSetDao.getSetNumber(trainingSetUid))
        } catch (e: Exception) {
            Resource.Error("Could not get set number for $trainingSetUid")
        }
    }

    override fun getSetsFromExercise(trainingExerciseUid: Long): Resource<Flow<List<TrainingSet>>> {
        return try {
            Resource.Success(trainingSetDao.getSetsFromTrainingExercise(trainingExerciseUid))
        } catch (e: Exception) {
            Resource.Error("Could not get sets for $trainingExerciseUid")
        }
    }


    override suspend fun getComment(trainingSetUid: Long): Resource<String> {
        return try {
            Resource.Success(trainingSetDao.getComment(trainingSetUid))
        } catch (e: Exception) {
            Resource.Error("Could not get comment for $trainingSetUid")
        }
    }

    override suspend fun updateComment(trainingSetUid: Long, comment: String) {
       trainingSetDao.updateComment(trainingSetUid, comment)
    }

    override suspend fun updateVideoPath(trainingSetUid: Long, videoPath: String) {
        trainingSetDao.updateVideoPath(trainingSetUid, videoPath)
    }

    override suspend fun getVideoPath(trainingSetUid: Long): Resource<String> {
        return try {
            Resource.Success(trainingSetDao.getVideoPath(trainingSetUid))
        } catch (e: Exception) {
            Resource.Error("Could not find video path for $trainingSetUid")
        }
    }

    override suspend fun delete(trainingExerciseUid: Long) {
        TODO("Not yet implemented")
    }

    override suspend fun countExercises(workoutUid: Long): Resource<Int> {
        TODO("Not yet implemented")
    }


}