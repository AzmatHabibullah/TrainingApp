package com.azmat.testdrivendevelopment


import android.content.Context
import androidx.room.Room
import com.azmat.testdrivendevelopment.data.models.TrainingApi
import com.azmat.testdrivendevelopment.db.MasterDatabase
import com.azmat.testdrivendevelopment.db.dao.*
import com.azmat.testdrivendevelopment.db.repo.*
import com.azmat.testdrivendevelopment.db.repo.defaultrepo.*
import com.azmat.testdrivendevelopment.utils.DispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject
import javax.inject.Singleton

private const val BASE_URL = "http://10.0.2.2:8083/"

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    // No need to cancel this scope as it'll be torn down with the process
    val applicationScope = CoroutineScope(SupervisorJob())

    @Singleton
    @Provides
    fun provideMasterDatabase(@ApplicationContext appContext: Context):
            MasterDatabase {
        return MasterDatabase.getDatabase(appContext, applicationScope)
    }

    @Singleton
    @Provides
    fun provideCategoryDao(database: MasterDatabase): CategoryDao = database.categoryDao()

    @Singleton
    @Provides
    fun provideExerciseDao(database: MasterDatabase): ExerciseDao = database.exerciseDao()

    @Singleton
    @Provides
    fun provideMuscleDao(database: MasterDatabase): MuscleDao = database.muscleDao()


    @Singleton
    @Provides
    fun provideTrainingSetDao(database: MasterDatabase): TrainingSetDao = database.trainingSetDao()


    @Singleton
    @Provides
    fun provideTrainingExerciseDao(database: MasterDatabase): TrainingExerciseDao = database.trainingExerciseDao()

    @Singleton
    @Provides
    fun provideTrainingExerciseWithSetDao(database: MasterDatabase): TrainingExerciseWithSetDao = database.trainingExerciseWithSetDao()


    @Singleton
    @Provides
    fun provideTrainingWorkoutDao(database: MasterDatabase): TrainingWorkoutDao = database.trainingWorkoutDao()


    @Singleton
    @Provides
    fun provideCategoryRepository(database: MasterDatabase): CategoryRepository =
        DefaultCategoryRepository(database.categoryDao())

    @Singleton
    @Provides
    fun provideMuscleRepository(database: MasterDatabase): MuscleRepository =
        DefaultMuscleRepository(database.muscleDao())

    @Singleton
    @Provides
    fun provideExerciseRepository(database: MasterDatabase): ExerciseRepository =
        DefaultExerciseRepository(database.exerciseDao(), database.categoryDao(), database.muscleDao())


    @Singleton
    @Provides
    fun provideTrainingSetRepository(database: MasterDatabase): TrainingSetRepository =
        DefaultTrainingSetRepository(database.trainingSetDao())

    @Singleton
    @Provides
    fun provideTrainingWorkoutRepository(database: MasterDatabase): TrainingWorkoutRepository =
        DefaultTrainingWorkoutRepository(database.trainingWorkoutDao())

    @Singleton
    @Provides
    fun provideTrainingExerciseRepository(database: MasterDatabase): TrainingExerciseRepository =
        DefaultTrainingExerciseRepository(database.trainingExerciseDao())

    @Singleton
    @Provides
    fun provideTrainingExerciseWithSetRepository(database: MasterDatabase): TrainingExerciseWithSetRepository =
        DefaultTrainingExerciseWithSetRepository(database.trainingExerciseWithSetDao())


    @Singleton
    @Provides
    fun provideTrainingApi(): TrainingApi = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(TrainingApi::class.java)

    @Singleton
    @Provides
    fun provideDispatchers(): DispatcherProvider = object : DispatcherProvider {
        override val main: CoroutineDispatcher
            get() = Dispatchers.Main
        override val io: CoroutineDispatcher
            get() = Dispatchers.IO
        override val default: CoroutineDispatcher
            get() = Dispatchers.Default
        override val unconfined: CoroutineDispatcher
            get() = Dispatchers.Unconfined

    }
}