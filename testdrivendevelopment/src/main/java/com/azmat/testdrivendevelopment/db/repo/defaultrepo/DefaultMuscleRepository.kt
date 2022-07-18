package com.azmat.testdrivendevelopment.db.repo.defaultrepo

import android.util.Log
import com.azmat.testdrivendevelopment.db.dao.MuscleDao
import com.azmat.testdrivendevelopment.db.model.Muscle
import com.azmat.testdrivendevelopment.db.repo.MuscleRepository
import com.azmat.testdrivendevelopment.utils.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DefaultMuscleRepository @Inject constructor(
    private val muscleDao: MuscleDao
) : MuscleRepository {
    override fun getMuscleNames(): Resource<Flow<List<String>>> {
        Log.d("DMR", "getting a muscle")
        return try {
            val names = muscleDao.getMuscleNames()
            Resource.Success(names)
        } catch (e: Exception) {
            Log.d("DMR", "failed")
            Resource.Error("Failed to get muscle names from DAO")
        }
    }

    override suspend fun addMuscle(muscleName: String): Resource<Long> {
        Log.d("DMR", "adding a muscle")
        return try {
            if (isNewMuscleNameValid(muscleName)) {
                Log.d("DMR", "here")
                Resource.Success(muscleDao.insert(Muscle(0, muscleName)))
            } else {
                Resource.Error("Cannot enter duplicate muscle")
            }
        } catch (e: Exception) {
            Log.d("DMR", "failed $e")
            Resource.Error("Failed to insert muscle $muscleName")
        }
    }

    override suspend fun isNewMuscleNameValid(muscleName: String): Boolean {
        return muscleDao.countMuscleName(muscleName) == 0
    }

    override suspend fun isExistingMuscleNameValid(muscleName: String): Boolean {
        return muscleDao.countMuscleName(muscleName) == 1
    }
}