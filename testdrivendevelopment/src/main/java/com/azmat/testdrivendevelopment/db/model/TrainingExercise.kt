package com.azmat.testdrivendevelopment.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "training_exercise_table")
data class TrainingExercise(
    @PrimaryKey(autoGenerate = true) val uid: Long,
    val exercise_uid: Long,
    val workout_uid: Long,
    val exercise_number: Int,
    val comment: String = ""
)
