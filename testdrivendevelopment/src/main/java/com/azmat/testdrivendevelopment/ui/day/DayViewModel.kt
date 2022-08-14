package com.azmat.testdrivendevelopment.ui.day

import android.util.Log
import androidx.lifecycle.*
import com.azmat.testdrivendevelopment.data.models.Event
import com.azmat.testdrivendevelopment.db.model.*
import com.azmat.testdrivendevelopment.db.repo.*
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
    trainingExerciseWithSetRepository: TrainingExerciseWithSetRepository,
     private val trainingWorkoutRepository: TrainingWorkoutRepository,
     private val dispatchers: DispatcherProvider,
     state: SavedStateHandle
) : ViewModel() {

    var categories: LiveData<List<String>> = categoryRepository.getCategoryNames().data!!.asLiveData()
    var exercises: LiveData<List<Exercise>> = exerciseRepository.getExercises().data!!.asLiveData()
    var exercisesWithSets: LiveData<List<TrainingExerciseWithSet>> = trainingExerciseWithSetRepository.getAllData().data!!.asLiveData()

    private var categoryFilter = MutableLiveData("%")

    private var _date = MutableLiveData<Long>(state.get<Long>(dateParam))
    var workoutsOnDate: LiveData<List<TrainingWorkout>> = trainingWorkoutRepository.getWorkoutsByDate(_date.value!!).data!!.asLiveData()


    private var _trainingExerciseId = MutableLiveData<Event<Long>>()
    var trainingExerciseId: LiveData<Event<Long>> = _trainingExerciseId

    private var _exerciseId = MutableLiveData<Long>()
    var exerciseId: LiveData<Long> = _exerciseId

    private var _workoutUid = MutableLiveData<Long>()
    var workoutUid: LiveData<Long> = _workoutUid
    var trainingExercisesInWorkout: LiveData<List<TrainingExercise>> = Transformations.switchMap(
        _workoutUid
    ) { workout_id -> trainingExerciseRepository.getInWorkout(workout_id).data!!.asLiveData() }


    fun setCategoryFilter(newFilter: String) {
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
            _exerciseId.postValue(exerciseUid)
            val exercise = TrainingExercise(
                uid = 0, exercise_uid = exerciseUid,
                workout_uid = _workoutUid.value!!,
                exercise_number = trainingExercisesInWorkout.value!!.size + 1
            )
            when (val result = trainingExerciseRepository.insert(exercise)) {
                is Resource.Success -> {
                    Log.d("DVM", "Added training exercise $exercise")
                    _trainingExerciseId.postValue(Event(result.data!!))
                }
                is Resource.Error -> {
                    Log.d("DVM", "Failed to add training exercise $exercise")
                }
            }
        }
    }

    fun initialiseWorkout() {
        viewModelScope.launch(dispatchers.io) {
            Log.d("DVM", "${_date.value}, ${workoutsOnDate.value}")
            if (workoutsOnDate.value == null) {
                Log.d("DVM", "workouts null, fetching data")
                Log.d("DVM", "${workoutsOnDate.value}")
            } else {
                val existingOnDate = workoutsOnDate.value!!.size
                if (existingOnDate == 0) {
                    Log.d("DVM", "no workouts today")
                    val workout = TrainingWorkout(0, _date.value!!, existingOnDate + 1)
                    when (val result = trainingWorkoutRepository.insert(workout)) {
                        is Resource.Success -> {
                            Log.d("DVM", "Added training workout $workout")
                            _workoutUid.postValue(result.data)
                        }
                        is Resource.Error -> {
                            Log.d("DVM", "Failed to add training workout $workout")
                        }
                    }
                } else {
                    _workoutUid.postValue(workoutsOnDate.value!![0].uid)
                    Log.d("DVM", "$existingOnDate workouts already exist")
                }
            }
        }
    }
}