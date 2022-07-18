package com.azmat.testdrivendevelopment.ui.add_exercise

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.azmat.testdrivendevelopment.databinding.FragmentAddExerciseBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddExerciseFragment : Fragment() {

    private var _binding: FragmentAddExerciseBinding? = null
    private val binding get() = _binding!!
    private val exerciseViewModel: AddExerciseViewModel by viewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentAddExerciseBinding.inflate(inflater, container, false)
        val view = binding.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnAddExerciseExerciseFragment.isEnabled = false

        defineState(exerciseViewModel.exerciseFormState,
            binding.btnAddExerciseExerciseFragment,
            binding.etExerciseNameExerciseFragment
        )
        defineResult(exerciseViewModel.addExerciseResult)

        val categoryAdapter = ArrayAdapter<String>(requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)
        val muscleAdapter = ArrayAdapter<String>(requireContext(),
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item)

        exerciseViewModel.categories.observe(viewLifecycleOwner,
            Observer { categories ->
                categoryAdapter.clear()
                categoryAdapter.addAll(categories)
                categoryAdapter.notifyDataSetChanged()
            })

        exerciseViewModel.muscles.observe(viewLifecycleOwner,
            Observer { muscles ->
                muscleAdapter.clear()
                muscleAdapter.addAll(muscles)
                muscleAdapter.notifyDataSetChanged()
            })

        binding.spCategoryExerciseFragment.adapter = categoryAdapter
        binding.spPrimaryMuscleExerciseFragment.adapter = muscleAdapter
        binding.spSecondaryMuscleExerciseFragment.adapter = muscleAdapter

        binding.btnAddExerciseExerciseFragment.setOnClickListener {
            val result = exerciseViewModel.addExercise(
                binding.etExerciseNameExerciseFragment.text.toString(),
                binding.spCategoryExerciseFragment.selectedItem.toString(),
                binding.spPrimaryMuscleExerciseFragment.selectedItem.toString(),
                binding.spSecondaryMuscleExerciseFragment.selectedItem.toString()
            )
        }

        val afterTextChangedListenerExercise = object: TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // nothing
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // nothing
            }

            override fun afterTextChanged(p0: Editable?) {
                exerciseViewModel.addExerciseDataChanged(
                    exerciseName = binding.etExerciseNameExerciseFragment.text.toString()
                )
            }
        }

        binding.etExerciseNameExerciseFragment.addTextChangedListener(afterTextChangedListenerExercise)

        val builder = AlertDialog.Builder(context)

        // todo add exercise database viewer with retrofit
        // todo clean architecture use cases for this

        binding.btnAddCategoryExerciseFragment.setOnClickListener {
            val newCategoryEditTextField = EditText(activity)
            with(builder) {
                setTitle("Add category")
                setPositiveButton("Add") { _, _ ->
                    val result = exerciseViewModel.addCategory(newCategoryEditTextField.text.toString())
                }
                setView(newCategoryEditTextField)
                setNegativeButton("Cancel") {dialog, _ ->
                    dialog.cancel()
                }
                create()
            }
            val dialog = builder.show()
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false

            defineState(exerciseViewModel.categoryFormState,
                dialog.getButton(AlertDialog.BUTTON_POSITIVE),
                newCategoryEditTextField
            )
            defineResult(exerciseViewModel.addCategoryResult)

            val afterTextChangedListener = object: TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    // nothing
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    // nothing
                }

                override fun afterTextChanged(p0: Editable?) {
                    exerciseViewModel.addCategoryDataChanged(
                        categoryName = newCategoryEditTextField.text.toString()
                    )
                }
            }
            newCategoryEditTextField.addTextChangedListener(afterTextChangedListener)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun defineResult(result: LiveData<AddResult>) {
        result.observe(viewLifecycleOwner,
            Observer { result ->
                result ?: return@Observer
                result.error?.let {
                    showAddFailed(it)
                }
                result.success?.let {
                    showAddSuccess(it)
                }
            })
        }

    private fun defineState(state: LiveData<AddFormState>, button: Button, editText: EditText) {
        state.observe(viewLifecycleOwner,
            Observer { addFormState ->
                Log.d("DS", "$addFormState")
                if (addFormState == null) return@Observer

                button.isEnabled = addFormState.isDataValid

                addFormState.nameError?.let {
                    editText.error = getString(it)
                }
            })
    }

    private fun showAddFailed(stringId: Int) {
        Toast.makeText(context, getString(stringId), Toast.LENGTH_SHORT).show()
    }

    private fun showAddSuccess(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
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
            AddExerciseFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}