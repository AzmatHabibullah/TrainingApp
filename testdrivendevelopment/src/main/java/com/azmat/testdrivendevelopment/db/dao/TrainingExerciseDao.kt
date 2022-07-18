package com.azmat.testdrivendevelopment.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.azmat.testdrivendevelopment.db.model.TrainingExercise
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainingExerciseDao {
    @Query("SELECT * FROM training_exercise_table ORDER BY exercise_number")
    fun getAll(): Flow<List<TrainingExercise>>

    @Query("SELECT * FROM training_exercise_table WHERE workout_uid in (:workoutUid)")
    fun getInWorkout(workoutUid: Long): Flow<List<TrainingExercise>>

    @Insert
    fun insertAll(vararg trainingExercises: TrainingExercise)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(trainingExercise: TrainingExercise) : Long

    @Query("DELETE FROM training_exercise_table")
    suspend fun deleteAll()

    // todo add @Delete and @Update
    @Query("DELETE FROM training_exercise_table where uid in (:trainingExerciseUid)")
    suspend fun delete(vararg trainingExerciseUid: Long)

    @Query("SELECT exercise_uid FROM training_exercise_table WHERE uid in (:trainingExerciseUid)")
    fun getExerciseUid(trainingExerciseUid: Long): Long
}