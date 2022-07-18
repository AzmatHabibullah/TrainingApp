package com.azmat.testdrivendevelopment.ui.day

data class AddTrainingExerciseResult (
    val success: LongArray? = null,
    val error: Int? = null
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as AddTrainingExerciseResult

        if (success != null) {
            if (other.success == null) return false
            if (!success.contentEquals(other.success)) return false
        } else if (other.success != null) return false
        if (error != other.error) return false

        return true
    }

    override fun hashCode(): Int {
        var result = success?.contentHashCode() ?: 0
        result = 31 * result + (error ?: 0)
        return result
    }
}