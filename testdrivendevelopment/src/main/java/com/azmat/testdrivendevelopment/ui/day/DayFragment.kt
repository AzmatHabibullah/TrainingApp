package com.azmat.testdrivendevelopment.ui.day

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.azmat.testdrivendevelopment.R
import com.azmat.testdrivendevelopment.databinding.FragmentDayBinding
import com.azmat.testdrivendevelopment.utils.DateUtils
import dagger.hilt.android.AndroidEntryPoint

private const val dateParam = "date"

@AndroidEntryPoint
class DayFragment : Fragment() {

    private var _binding: FragmentDayBinding? = null
    private val binding get() = _binding!!
    private val dayViewModel: DayViewModel by viewModels()

    val args: DayFragmentArgs by navArgs()

    private var date: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        date = args.date
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDayBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val newBuilder = AlertDialog.Builder(context)

        // todo check if this is date or datetime

        dayViewModel.categories.observe(viewLifecycleOwner, androidx.lifecycle.Observer {  })
        dayViewModel.filteredExercises.observe(viewLifecycleOwner, androidx.lifecycle.Observer {  })
        dayViewModel.workoutsOnDate.observe(viewLifecycleOwner, Observer {it ->
            Log.d(tag, "$it")
        })

        dayViewModel.addTrainingExerciseResult.observe(viewLifecycleOwner, Observer {
            result ->
            result ?: return@Observer
            result.error?.let {
                Log.d(tag, "Failed to add exercise")
            }
            result.success?.let {
                val bundle = bundleOf("exerciseUid" to it[1],
                "trainingExerciseUid" to it[0],
                "workoutUid" to it[2])
                findNavController().navigate(R.id.action_dayFragment_to_exerciseFragment, bundle)
            }
        })

        dayViewModel.trainingExercisesInWorkout.observe(viewLifecycleOwner, Observer {  })


        binding.dateTVDayF.text = DateUtils.formatDate(date)

        // todo convert this to a separate DialogFragment
        binding.addFABDayF.setOnClickListener {
            val builder = AlertDialog.Builder(context)
            dayViewModel.initialiseWorkout()
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

                    dayViewModel.setFilter(category)
                    val filteredExercises = dayViewModel.filteredExercises.value!!

                    newBuilder.setNegativeButton("Back") { exerciseDialog, _ ->
                        exerciseDialog.cancel()
                        builder.create().show()
                    }

                    newBuilder.setPositiveButton("Add exercise") { exerciseDialog, _ ->
                        exerciseDialog.cancel()
                        // todo add category persistence via bundleOf
                        findNavController().navigate(R.id.action_dayFragment_to_addExerciseFragment)
                    }

                    newBuilder.setTitle("Pick exercise")
                    newBuilder.setItems(filteredExercises.map{it.exercise_name}.toTypedArray()) {
                        _, selectedExerciseId ->
                        val selectedExercise = filteredExercises[selectedExerciseId]
                        Log.d(tag, "selected exercise ${selectedExercise.exercise_name}")
                        dayViewModel.insertTrainingExercise(selectedExercise.exercise_uid)
                    }

                    newBuilder.create().show()
                }
            }
            builder.create().show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment AddExerciseFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            DayFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

}