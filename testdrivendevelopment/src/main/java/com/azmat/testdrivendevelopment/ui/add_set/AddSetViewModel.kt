package com.azmat.testdrivendevelopment.ui.add_set

import android.util.Log
import androidx.lifecycle.*
import com.azmat.testdrivendevelopment.R
import com.azmat.testdrivendevelopment.db.model.Exercise
import com.azmat.testdrivendevelopment.db.model.TrainingSet
import com.azmat.testdrivendevelopment.db.repo.ExerciseRepository
import com.azmat.testdrivendevelopment.db.repo.TrainingExerciseRepository
import com.azmat.testdrivendevelopment.db.repo.TrainingSetRepository
import com.azmat.testdrivendevelopment.ui.add_exercise.AddResult
import com.azmat.testdrivendevelopment.utils.DispatcherProvider
import com.azmat.testdrivendevelopment.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import hilt_aggregated_deps._com_azmat_testdrivendevelopment_ui_add_set_ExerciseFragment_GeneratedInjector
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val trainingExerciseUidParam = "trainingExerciseUid"
private const val exerciseUidParam = "exerciseUid"
private const val workoutUidParam = "workoutUid"

@HiltViewModel
class AddSetViewModel @Inject constructor(
    private val exerciseRepository: ExerciseRepository,
    private val trainingSetRepository: TrainingSetRepository,
    private val dispatchers: DispatcherProvider,
    state: SavedStateHandle
) : ViewModel() {

    private var _trainingExerciseUid = state.get<Long>(trainingExerciseUidParam)
    private var _workoutUid = state.get<Long>(workoutUidParam)

    var trainingSets: LiveData<List<TrainingSet>> = trainingSetRepository.getSetsFromExercise(_trainingExerciseUid!!).data!!.asLiveData()

    private val _setForm = MutableLiveData<AddSetFormState>()
    val addSetFormState: LiveData<AddSetFormState> = _setForm

    private val _addSetResult = MutableLiveData<AddSetResult>()
    val addSetResult: LiveData<AddSetResult> = _addSetResult


    fun getExerciseFromId(exerciseUid: Long) : Resource<Exercise> = runBlocking(Dispatchers.Default) {
        val result = withContext(Dispatchers.Default) {
            exerciseRepository.getExerciseFromId(exerciseUid)
        }
        return@runBlocking result
    }

    fun addSet(weight: Double, reps: Int) {
        viewModelScope.launch(dispatchers.io) {
            val set = TrainingSet(0, reps=reps, weight=weight,
                training_exercise_uid=_trainingExerciseUid!!, workout_uid = _workoutUid!!,
            set_number = trainingSets.value!!.size + 1)
            when (val result = trainingSetRepository.insert(set)) {
                is Resource.Success -> _addSetResult.postValue(AddSetResult("Added set"))
                is Resource.Error ->
                    if (result.message?.contains("weight") == true) {
                        _addSetResult.postValue(AddSetResult(error = R.string.set_weight_pos))
                    } else {
                        _addSetResult.postValue(AddSetResult(error = R.string.set_reps_pos))
                    }
            }
        }
    }

    fun addSetDataChanged(weight: Double?, reps: Int?) {
        when (reps) {
            null -> {
                _setForm.value = AddSetFormState(repsError = R.string.set_reps_blank)
            }
            else -> {
                _setForm.value = AddSetFormState(isDataValid = true)
            }
        }
    }

}
