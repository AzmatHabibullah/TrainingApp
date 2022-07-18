package com.azmat.testdrivendevelopment.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.azmat.testdrivendevelopment.db.model.TrainingWorkout
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainingWorkoutDao {
    @Query("SELECT * FROM training_workout_table")
    fun getAll(): List<TrainingWorkout>

    @Query("SELECT * FROM training_workout_table ORDER BY date ASC")
    fun getWorkouts(): Flow<List<TrainingWorkout>>

    @Query("SELECT * FROM training_workout_table WHERE date in (:date)")
    fun loadAllByDate(date: Long): Flow<List<TrainingWorkout>>

    @Query("SELECT date FROM training_workout_table WHERE uid in (:workout_uid)")
    fun getDate(workout_uid: Long): Long

    @Insert
    fun insertAll(vararg workouts: TrainingWorkout)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(workout: TrainingWorkout) : Long // returns primary key

    @Query("DELETE FROM training_workout_table")
    suspend fun deleteAll()

    // todo add @Delete and @Update
    @Query("DELETE FROM training_workout_table where uid in (:trainingWorkoutUid)")
    suspend fun delete(vararg trainingWorkoutUid: Long)

}