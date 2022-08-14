package com.azmat.testdrivendevelopment.db.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Relation

data class TrainingExerciseWithSet(
    @Embedded(prefix = "tex_") val trainingExercise: TrainingExercise,
    @Relation(
        parentColumn = "tex_uid",
        entityColumn = "training_exercise_uid"
    )
    val trainingSets: List<TrainingSet>,
    @Embedded(prefix = "ex_") val exercise: Exercise,
)
