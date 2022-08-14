package com.azmat.testdrivendevelopment.ui.day

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.azmat.testdrivendevelopment.R
import com.azmat.testdrivendevelopment.adapters.TrainingExerciseListAdapter
import com.azmat.testdrivendevelopment.adapters.TrainingExerciseWithSetListAdapter
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

        dayViewModel.categories.observe(viewLifecycleOwner, androidx.lifecycle.Observer {  })
        dayViewModel.filteredExercises.observe(viewLifecycleOwner, androidx.lifecycle.Observer {

        })
        dayViewModel.workoutsOnDate.observe(viewLifecycleOwner, Observer {it ->
            it.let {
                Log.d(tag, "workouts are $it")
                dayViewModel.initialiseWorkout()
            }
        })


        val exerciseSetAdapter =
            TrainingExerciseWithSetListAdapter { trainingExerciseUid, exerciseUid, exerciseName, workoutUid ->
                navigateToSetFragment(trainingExerciseUid, exerciseUid, workoutUid)
            }

        dayViewModel.trainingExerciseId.observe(viewLifecycleOwner, Observer {
            id ->
            id.getContentIfNotHandled()?.let { // only proceed if the event has never been handled
                navigateToSetFragment(id.peekContent(), dayViewModel.exerciseId.value!!,
                dayViewModel.workoutUid.value!!)
            }
        })

        dayViewModel.exerciseId.observe(viewLifecycleOwner, Observer {  })
        dayViewModel.trainingExercisesInWorkout.observe(viewLifecycleOwner, Observer {  })

        dayViewModel.exercisesWithSets.observe(viewLifecycleOwner, Observer {
            exercisesWithSets ->
            exercisesWithSets.let {
                exerciseSetAdapter.submitList(exercisesWithSets.filter{it.trainingExercise.workout_uid == dayViewModel.workoutUid.value}.distinct())
            }
        })


        binding.setsRVDayF.adapter = exerciseSetAdapter
        binding.setsRVDayF.layoutManager = LinearLayoutManager(activity)

        binding.dateTVDayF.text = DateUtils.formatDate(date)

        binding.addFABDayF.setOnClickListener {
            NewExerciseDialogFragment(dayViewModel).show(childFragmentManager,
            NewExerciseDialogFragment.TAG)
        }
    }

    private fun navigateToSetFragment(
        trainingExerciseUid: Long,
        exerciseUid: Long,
        workoutUid: Long
    ) {
        val action = DayFragmentDirections.actionDayFragmentToExerciseFragment(
            exerciseUid = exerciseUid,
            trainingExerciseUid = trainingExerciseUid,
            workoutUid = workoutUid)
        findNavController().navigate(action)
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