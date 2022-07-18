package com.azmat.testdrivendevelopment.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "category_table")
data class Category(
    @PrimaryKey(autoGenerate = true) val category_uid: Int,
    val category_name: String
)