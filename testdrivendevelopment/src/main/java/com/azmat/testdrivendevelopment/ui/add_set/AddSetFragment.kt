package com.azmat.testdrivendevelopment.ui.add_set

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.azmat.testdrivendevelopment.adapters.TrainingSetListAdapter
import com.azmat.testdrivendevelopment.databinding.FragmentAddSetBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddSetFragment : Fragment() {

    private var _binding: FragmentAddSetBinding? = null
    private val binding get() = _binding!!
    private val addSetViewModel: AddSetViewModel by viewModels()

    private var exerciseUid: Long? = null
    private var trainingExerciseUid: Long? = null
    private var workoutUid: Long? = null

    val args: AddSetFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            exerciseUid = args.exerciseUid
            trainingExerciseUid = args.trainingExerciseUid
            workoutUid = args.workoutUid
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentAddSetBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // todo add buttons to increment weight and reps

        binding.exerciseTVExerciseF.text =
            addSetViewModel.getExerciseFromId(exerciseUid!!).data!!.exercise_name

        addSetViewModel.addSetFormState.observe(viewLifecycleOwner,
            Observer { addSetState ->
                if (addSetState == null) return@Observer

                binding.addBExerciseF.isEnabled = addSetState.isDataValid // todo why is this not working initially

                addSetState.repsError?.let {
                    binding.repsETExerciseF.error = getString(it)
                }
                addSetState.weightError?.let {
                    binding.weightETExerciseF.error = getString(it)
                }
            })

        addSetViewModel.addSetResult.observe(viewLifecycleOwner,
            Observer { result ->
                result ?: return@Observer
                result.error?.let {
                    // todo
                }
                result.success?.let {
                    Toast.makeText(context, "Added set", Toast.LENGTH_SHORT).show() // todo remove once recyclerview added
                }
            })

        val adapter = TrainingSetListAdapter(
            {},
            {}
        )

        // TODO: fix entry with 0 text for weight or reps

        addSetViewModel.trainingSets.observe(viewLifecycleOwner, Observer {
            trainingSets ->
            trainingSets.let {
                adapter.submitList(it)
            }
        })

        binding.setsRVExerciseF.adapter = adapter
        binding.setsRVExerciseF.layoutManager = LinearLayoutManager(requireActivity())

        binding.addBExerciseF.setOnClickListener {
            addSetViewModel.addSet(weight = (binding.weightETExerciseF.text.toString().toDoubleOrNull() ?: 0.0),
                binding.repsETExerciseF.text.toString().toInt())
        }

        val afterTextChangedListenerExercise = object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                addSetViewModel.addSetDataChanged(
                    weight = binding.weightETExerciseF.text.toString().toDoubleOrNull(),
                    reps = binding.repsETExerciseF.text.toString().toIntOrNull()
                )
            }

        }

        binding.repsETExerciseF.addTextChangedListener(afterTextChangedListenerExercise)
        binding.weightETExerciseF.addTextChangedListener(afterTextChangedListenerExercise)

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
         * @return A new instance of fragment AddSetBinding.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddSetFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

}