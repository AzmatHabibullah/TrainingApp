package com.azmat.testdrivendevelopment

object ExerciseUtil {

    private val existingExercises = listOf("Squat", "Deadlift")

    /**
     * input not valid if...
     * ... anything is empty
     * ... exerciseName is already taken
     */
    fun validateNewExerciseInput(
        exerciseName: String,
        category: String,
        primaryMuscle: String,
        secondaryMuscle: String
    ) : Boolean {
        if (exerciseName.isBlank() || category.isBlank() ||
            primaryMuscle.isBlank() || secondaryMuscle.isBlank()) {
            return false
        }

        if (exerciseName.trim() in existingExercises) {
            return false
        }

        return true
    }

}