package com.azmat.testdrivendevelopment.ui.day

import android.util.Log
import androidx.lifecycle.*
import com.azmat.testdrivendevelopment.db.model.Exercise
import com.azmat.testdrivendevelopment.db.model.TrainingExercise
import com.azmat.testdrivendevelopment.db.model.TrainingWorkout
import com.azmat.testdrivendevelopment.db.repo.CategoryRepository
import com.azmat.testdrivendevelopment.db.repo.ExerciseRepository
import com.azmat.testdrivendevelopment.db.repo.TrainingExerciseRepository
import com.azmat.testdrivendevelopment.db.repo.TrainingWorkoutRepository
import com.azmat.testdrivendevelopment.utils.DispatcherProvider
import com.azmat.testdrivendevelopment.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val dateParam = "date"

@HiltViewModel
class DayViewModel @Inject constructor(
    val exerciseRepository: ExerciseRepository,
     categoryRepository: CategoryRepository,
     private val trainingExerciseRepository: TrainingExerciseRepository,
     private val trainingWorkoutRepository: TrainingWorkoutRepository,
     private val dispatchers: DispatcherProvider,
     state: SavedStateHandle
) : ViewModel() {

    var categories: LiveData<List<String>> = categoryRepository.getCategoryNames().data!!.asLiveData()
    var exercises: LiveData<List<Exercise>> = exerciseRepository.getExercises().data!!.asLiveData()

    private var categoryFilter = MutableLiveData("%")

    private var _date = MutableLiveData<Long>(state.get<Long>(dateParam))
    var workoutsOnDate: LiveData<List<TrainingWorkout>> = trainingWorkoutRepository.getWorkoutsByDate(_date.value!!).data!!.asLiveData()

    private var _addTrainingExerciseResult = MutableLiveData<AddTrainingExerciseResult>()
    var addTrainingExerciseResult: LiveData<AddTrainingExerciseResult> = _addTrainingExerciseResult

    var workoutUid = MutableLiveData<Long>()
    var trainingExercisesInWorkout: LiveData<List<TrainingExercise>> = Transformations.switchMap(
        workoutUid
    ) { workout_id -> trainingExerciseRepository.getInWorkout(workout_id).data!!.asLiveData() }


    fun setFilter(newFilter: String) {
        categoryFilter.value = newFilter // apply the filter
    }

    // Exposed to the view
    val filteredExercises = MediatorLiveData<List<Exercise>>().apply {
        addSource(exercises) { combine(it, categoryFilter.value) }
        addSource(categoryFilter) { combine(exercises.value, it) }
    }

    private fun combine(exercises: List<Exercise>?, category: String?) {
        if (exercises == null) {
            return
        } // Don't try to work if your "base data" is not yet present
        filteredExercises.value = if (category == null) {
            return
        } else {
            exercises.filter { it.category.category_name == category }
        }
    }

    fun insertTrainingExercise(exerciseUid: Long) {
        viewModelScope.launch(dispatchers.io) {
            val exercise = TrainingExercise(0, exerciseUid, workoutUid.value!!,
                trainingExercisesInWorkout.value!!.size + 1)
            when (val result = trainingExerciseRepository.insert(exercise)) {
                is Resource.Success -> {
                    Log.d("DVM", "Added training exercise $exercise")
                    _addTrainingExerciseResult.postValue(AddTrainingExerciseResult(
                        longArrayOf(result.data!!, exerciseUid, workoutUid.value!!)))
                }
                is Resource.Error -> {
                    Log.d("DVM", "Failed to add training exercise $exercise")
                    _addTrainingExerciseResult.postValue(AddTrainingExerciseResult(error = -1)) // todo
                }
            }
        } // todo how do I get value of UID inserted
    }

    fun initialiseWorkout() {
        viewModelScope.launch(dispatchers.io) {
            Log.d("DVM", "${_date.value}, ${workoutsOnDate.value}")
            if (workoutsOnDate.value == null) {
                Log.d("DVM", "workouts null")
            } else {
                val existingOnDate = workoutsOnDate.value!!.size
                if (existingOnDate == 0) {
                    val workout = TrainingWorkout(0, _date.value!!, existingOnDate + 1)
                    when (val result = trainingWorkoutRepository.insert(workout)) {
                        is Resource.Success -> {
                            Log.d("DVM", "Added training workout $workout")
                            workoutUid.postValue(result.data)
                        }
                        is Resource.Error -> {
                            Log.d("DVM", "Failed to add training workout $workout")
                        }
                    }
                } else {
                    workoutUid.postValue(workoutsOnDate.value!!.get(0).uid)
                    Log.d("DVM", "Workout already exists")
                }
            }
        }
    }
}

// todo parcelable