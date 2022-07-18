package com.azmat.testdrivendevelopment

import org.junit.Test
import org.junit.Assert.*

class ExerciseUtilTest {

    @Test
    fun `blank exercise name returns false`() {
        val result = ExerciseUtil.validateNewExerciseInput(
            "",
            "Legs",
            "Quads",
            "Hamstrings"
        )
        assertEquals(result, false)
    }

    @Test
    fun `blank category returns false`() {
        val result = ExerciseUtil.validateNewExerciseInput(
            "Squat",
            "",
            "",
            ""
        )
        assertEquals(result, false)
    }

    @Test
    fun `blank primary muscle returns false`() {
        val result = ExerciseUtil.validateNewExerciseInput(
            "Squat",
            "Legs",
            "  ",
            "Quadriceps"
        )
        assertEquals(result, false)
    }

    @Test
    fun `blank secondary muscle returns false`() {
        val result = ExerciseUtil.validateNewExerciseInput(
            "Deadlift",
            "Back",
            "Lower back",
            " "
        )
        assertEquals(result, false)
    }

    @Test
    fun `duplicate exercise name returns false`() {
        val result = ExerciseUtil.validateNewExerciseInput(
            exerciseName = "Deadlift",
            category = "Legs",
            primaryMuscle = "Quads",
            secondaryMuscle = "Hamstrings"
        )
        assertEquals(result, false)
    }
}