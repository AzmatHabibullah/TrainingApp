package com.azmat.testdrivendevelopment.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.azmat.testdrivendevelopment.db.model.Exercise
import com.azmat.testdrivendevelopment.db.model.TrainingSet
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainingSetDao {
    @Query("SELECT * FROM training_set_table")
    fun getAll(): List<TrainingSet>

    @Query("SELECT * FROM training_set_table")
    fun getSets(): Flow<List<TrainingSet>>

    @Query("SELECT * FROM training_set_table " +
            "JOIN exercise_table ON training_set_table.training_exercise_uid = exercise_table.exercise_uid")
    fun getExercises(): Flow<List<Exercise>>

    @Query("SELECT * FROM training_set_table WHERE training_exercise_uid = :trainingExerciseUid " +
            "ORDER BY training_exercise_uid")  // todo add sense of time
    fun getSetsFromTrainingExercise(trainingExerciseUid: Long): Flow<List<TrainingSet>>

    @Query("UPDATE training_set_table SET comment = :comment WHERE uid = :training_set_uid")
    fun updateComment(training_set_uid: Long, comment: String): Int

    @Query("UPDATE training_set_table SET video_path = :video_path WHERE uid = :training_set_uid")
    fun updateVideoPath(training_set_uid: Long, video_path: String): Int

    @Query("SELECT video_path FROM training_set_table WHERE uid = :training_set_uid")
    fun getVideoPath(training_set_uid: Long): String

    @Query("SELECT set_number FROM training_set_table WHERE uid = :training_set_uid")
    fun getSetNumber(training_set_uid: Long): Int

    @Query("SELECT comment FROM training_set_table WHERE uid = :training_set_uid")
    fun getComment(training_set_uid: Long): String

    @Insert
    fun insertAll(vararg trainingSets: TrainingSet)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(trainingSet: TrainingSet): Long

    @Query("DELETE FROM training_set_table")
    suspend fun deleteAll()

}