package com.azmat.testdrivendevelopment.ui.add_exercise

import android.util.Log
import androidx.lifecycle.*
import com.azmat.testdrivendevelopment.R
import com.azmat.testdrivendevelopment.data.models.TrainingApi
import com.azmat.testdrivendevelopment.db.repo.CategoryRepository
import com.azmat.testdrivendevelopment.db.repo.ExerciseRepository
import com.azmat.testdrivendevelopment.db.repo.MuscleRepository
import com.azmat.testdrivendevelopment.utils.DispatcherProvider
import com.azmat.testdrivendevelopment.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddExerciseViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val categoryRepository: CategoryRepository,
    private val muscleRepository: MuscleRepository,
    private val dispatchers: DispatcherProvider,
    private val apiInterface: TrainingApi
) : ViewModel() {

    private val _exerciseForm = MutableLiveData<AddFormState>()
    val exerciseFormState: LiveData<AddFormState> = _exerciseForm

    private val _addExerciseResult = MutableLiveData<AddResult>()
    val addExerciseResult: LiveData<AddResult> = _addExerciseResult

    var categories: LiveData<List<String>> =
        categoryRepository.getCategoryNames().data!!.asLiveData()

    var muscles: LiveData<List<String>> =
        muscleRepository.getMuscleNames().data!!.asLiveData()

    fun addExercise(exerciseName: String, categoryName: String, primaryMuscleName: String, secondaryMuscleName: String) {
        viewModelScope.launch(dispatchers.io) {
            when (val result = exerciseRepository.insert(exerciseName, categoryName, primaryMuscleName, secondaryMuscleName)) {
                is Resource.Success -> {
                    _addExerciseResult.postValue(AddResult("Added $exerciseName"))
                    Log.d("AEVM", apiInterface.getExercise().body().toString())
                }
                is Resource.Error ->
                    if (result.message?.contains("duplicate") == true) {
                        _addExerciseResult.postValue(AddResult(error = R.string.exercise_duplicate))
                    } else {
                        _addExerciseResult.postValue(AddResult(error = R.string.exercise_blank))
                    }
            }
        }
    }

    fun addExerciseDataChanged(exerciseName: String) {
        when {
            isBlank(exerciseName) -> {
                _exerciseForm.value = AddFormState(nameError = R.string.exercise_blank)
            }
            isExerciseDuplicate(exerciseName) -> {
                _exerciseForm.value = AddFormState(nameError = R.string.exercise_duplicate)
            }
            else -> {
                _exerciseForm.value = AddFormState(isDataValid = true)
            }
        }
    }

    private val _categoryForm = MutableLiveData<AddFormState>()
    val categoryFormState: LiveData<AddFormState> = _categoryForm

    private val _addCategoryResult = MutableLiveData<AddResult>()
    val addCategoryResult: LiveData<AddResult> = _addCategoryResult

    fun addCategory(categoryName: String) {
        viewModelScope.launch(dispatchers.io) {
            // result loading
            when (val result = categoryRepository.addCategory(categoryName)) {
                is Resource.Success -> _addCategoryResult.postValue(AddResult(success = "Added $categoryName category"))
                is Resource.Error ->
                    if (result.message?.contains("duplicate") == true) {
                        _addCategoryResult.postValue(AddResult(error = R.string.category_duplicate))
                    } else {
                        _addCategoryResult.postValue(AddResult(error = R.string.category_add_failed))
                    }
            }
        }
    }

    fun addCategoryDataChanged(categoryName: String) {
        when {
            isBlank(categoryName) -> {
                _categoryForm.value = AddFormState(nameError = R.string.category_blank)
            }
            isCategoryDuplicate(categoryName) -> {
                _categoryForm.value = AddFormState(nameError = R.string.category_duplicate)
            }
            else -> {
                _categoryForm.value = AddFormState(isDataValid = true)
            }
        }
    }

    private fun isBlank(textEntry: String): Boolean {
        return textEntry.trim().isBlank()
    }

    private fun isExerciseDuplicate(exerciseName: String): Boolean {
        return false // TODO
    }

    private fun isCategoryDuplicate(categoryName: String): Boolean {
        return false // TODO
    }

}