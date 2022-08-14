package com.azmat.testdrivendevelopment.db.repo.defaultrepo

import com.azmat.testdrivendevelopment.db.dao.TrainingExerciseWithSetDao
import com.azmat.testdrivendevelopment.db.model.Exercise
import com.azmat.testdrivendevelopment.db.model.TrainingExerciseWithSet
import com.azmat.testdrivendevelopment.db.repo.TrainingExerciseWithSetRepository
import com.azmat.testdrivendevelopment.utils.Resource
import kotlinx.coroutines.flow.Flow

class DefaultTrainingExerciseWithSetRepository(private val trainingExerciseWithSetDao: TrainingExerciseWithSetDao) :
TrainingExerciseWithSetRepository{

    override fun getAllData(): Resource<Flow<List<TrainingExerciseWithSet>>> {
        return try {
            Resource.Success(trainingExerciseWithSetDao.getAllData())
        } catch (e: Exception) {
            Resource.Error("Could not get all training data")
        }
    }

}