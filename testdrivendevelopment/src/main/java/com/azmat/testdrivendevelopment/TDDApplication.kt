package com.azmat.testdrivendevelopment

import android.app.Application
import com.azmat.testdrivendevelopment.db.MasterDatabase
import com.azmat.testdrivendevelopment.db.repo.defaultrepo.DefaultExerciseRepository
import com.azmat.testdrivendevelopment.db.repo.ExerciseRepository
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@HiltAndroidApp
class TDDApplication : Application() {

}