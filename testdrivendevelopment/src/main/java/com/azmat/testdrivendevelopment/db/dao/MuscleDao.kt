package com.azmat.testdrivendevelopment.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.azmat.testdrivendevelopment.db.model.Muscle
import kotlinx.coroutines.flow.Flow

@Dao
interface MuscleDao {
    @Insert
    suspend fun insert(muscle: Muscle): Long

    @Insert
    suspend fun insert(vararg categories: Muscle): List<Long>

    @Query("SELECT muscle_name FROM muscle_table")
    fun getMuscleNames() : Flow<List<String>>

    @Query("SELECT count(*) FROM muscle_table WHERE muscle_name=:muscleNameToCheck")
    fun countMuscleName(muscleNameToCheck: String): Int

    @Query("SELECT * FROM muscle_table WHERE muscle_name=:muscleName")
    fun getMuscleByName(muscleName: String) : Muscle

    @Delete
    suspend fun delete(muscle: Muscle)

    @Query("DELETE from muscle_table")
    suspend fun deleteAll()
}