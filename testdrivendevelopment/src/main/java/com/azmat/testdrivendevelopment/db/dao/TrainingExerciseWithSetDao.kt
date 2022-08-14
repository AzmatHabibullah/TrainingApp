package com.azmat.testdrivendevelopment.db.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.azmat.testdrivendevelopment.db.model.TrainingExerciseWithSet
import kotlinx.coroutines.flow.Flow

@Dao
interface TrainingExerciseWithSetDao {
    @Transaction
    @Query(
        """
        SELECT 
            training_exercise_table.uid AS tex_uid,
            training_exercise_table.exercise_uid AS tex_exercise_uid,
            training_exercise_table.workout_uid AS tex_workout_uid,
            training_exercise_table.exercise_number AS tex_exercise_number,
            training_exercise_table.comment AS tex_comment,
            exercise_table.uid AS ex_uid,
            exercise_table.exercise_name AS ex_exercise_name,
            exercise_table.category_uid AS ex_category_uid,
            exercise_table.category_name AS ex_category_name,
            exercise_table.primary_muscle_name AS ex_primary_muscle_name,
            exercise_table.secondary_muscle_name AS ex_secondary_muscle_name,
            exercise_table.primary_muscle_uid AS ex_primary_muscle_uid,
            exercise_table.secondary_muscle_uid AS ex_secondary_muscle_uid
        FROM training_exercise_table
        LEFT JOIN exercise_table ON training_exercise_table.exercise_uid = ex_uid
        LEFT JOIN training_set_table ON training_set_table.training_exercise_uid = tex_uid"""
    )
    fun getAllData(): Flow<List<TrainingExerciseWithSet>>
}
