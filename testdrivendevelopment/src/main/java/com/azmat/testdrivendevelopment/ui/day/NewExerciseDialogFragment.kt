package com.azmat.testdrivendevelopment.ui.day

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.navigation.fragment.findNavController
import com.azmat.testdrivendevelopment.R

class NewExerciseDialogFragment(
    private val dayViewModel: DayViewModel
) : DialogFragment(

) {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // todo with categories and exercises in constructor, build this
        val builder = AlertDialog.Builder(context)
        val categories = dayViewModel.categories.value

        with(builder) {
            setTitle("Pick category")
            Log.d(tag, "$categories")
            setNegativeButton("Cancel") { catDialog, id ->
                catDialog.cancel()
                Log.d(tag, "Cancelled FAB")
            }
            setPositiveButton("Add exercise") { exerciseDialog, _ ->
                exerciseDialog.cancel()
                findNavController().navigate(R.id.action_dayFragment_to_addExerciseFragment)
            }

            setItems(categories?.toTypedArray()) {_, selectedCategory ->
                val category = categories?.get(selectedCategory) ?: "N/A"

                dayViewModel.setCategoryFilter(category)
                val filteredExercises = dayViewModel.filteredExercises.value!!

                val newBuilder = AlertDialog.Builder(context)

                newBuilder.setNegativeButton("Back") { exerciseDialog, _ ->
                    exerciseDialog.cancel()
                    builder.create().show()
                }

                newBuilder.setPositiveButton("Add exercise") { exerciseDialog, _ ->
                    exerciseDialog.cancel()
                    findNavController().navigate(R.id.action_dayFragment_to_addExerciseFragment)
                } // todo add category navigation

                newBuilder.setTitle("Pick exercise")
                newBuilder.setItems(filteredExercises.map{it.exercise_name}.toTypedArray()) {
                        _, selectedExerciseId ->
                    val selectedExercise = filteredExercises[selectedExerciseId]
                    Log.d(tag, "selected exercise ${selectedExercise.exercise_name}")
                    dayViewModel.insertTrainingExercise(selectedExercise.uid)
                }

                newBuilder.create().show()
            }
        }
        return builder.create()

    }

    companion object {
        const val TAG = "NewExerciseDialogFragment"
    }

}