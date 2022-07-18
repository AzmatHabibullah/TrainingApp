package com.azmat.testdrivendevelopment.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "muscle_table")
data class Muscle(
    @PrimaryKey(autoGenerate = true) val muscle_uid: Int,
    val muscle_name: String
)