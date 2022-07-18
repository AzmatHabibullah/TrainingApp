package com.azmat.testdrivendevelopment.db.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.Relation

@Entity(tableName = "exercise_table")
data class Exercise (
    @PrimaryKey(autoGenerate = true) val exercise_uid: Long,
    val exercise_name: String,
    @Embedded val category: Category,
    @Embedded(prefix="primary_") val primary_muscle: Muscle,
    @Embedded(prefix="secondary_") val secondary_muscle: Muscle
)