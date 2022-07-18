package com.azmat.testdrivendevelopment.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "training_workout_table")
data class TrainingWorkout(
    @PrimaryKey(autoGenerate = true) val uid: Long,
    @ColumnInfo(name = "date") val date: Long,
    @ColumnInfo(name = "workout_number") val workout_number: Int,
    val comment: String = ""
)