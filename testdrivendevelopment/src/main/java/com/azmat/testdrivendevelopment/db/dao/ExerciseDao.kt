package com.azmat.testdrivendevelopment.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.azmat.testdrivendevelopment.db.model.Exercise
import kotlinx.coroutines.flow.Flow

@Dao
interface ExerciseDao {
    @Insert
    suspend fun insert(exercise: Exercise): Long

    @Insert
    suspend fun insert(vararg exercises: Exercise): List<Long>

    @Query("SELECT * FROM exercise_table")
    fun getExercises(): Flow<List<Exercise>>

    @Query("SELECT exercise_name FROM exercise_table")
    fun getExerciseNames() : Flow<List<String>>

    @Query("SELECT count(*) FROM exercise_table WHERE exercise_name=:exerciseNameToCheck")
    fun countExerciseName(exerciseNameToCheck: String): Int

    @Query("SELECT exercise_name FROM exercise_table WHERE category_name=:categoryNameToCheck")
    fun getExercisesInCategory(categoryNameToCheck: String): Flow<List<String>>

    @Query("SELECT * FROM exercise_table WHERE exercise_uid=:exerciseUid LIMIT 1")
    fun getExerciseFromUid(exerciseUid: Long): Exercise

    @Delete
    suspend fun delete(exercise: Exercise)

    @Query("DELETE FROM exercise_table")
    suspend fun deleteAll()
}