package com.azmat.testdrivendevelopment.db.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "training_set_table")

data class TrainingSet(
    @PrimaryKey(autoGenerate = true) val uid: Long,
    @ColumnInfo(name = "reps") val reps: Int,
    @ColumnInfo(name = "weight") val weight: Double,
    val set_number: Int,
    val training_exercise_uid: Long,
    val workout_uid: Long, // todo develop
    @ColumnInfo(name = "unit") val unit: String = "kg",
    val comment: String = "",
    val video_path: String = ""
) : Serializable
