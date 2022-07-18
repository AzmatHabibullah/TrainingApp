package com.azmat.testdrivendevelopment.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.azmat.testdrivendevelopment.db.dao.*
import com.azmat.testdrivendevelopment.db.model.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Database(entities=[Category::class, Exercise::class, Muscle::class, TrainingSet::class, TrainingExercise::class, TrainingWorkout::class], version = 1, exportSchema = false)
abstract class MasterDatabase : RoomDatabase() {

    abstract fun categoryDao(): CategoryDao
    abstract fun exerciseDao(): ExerciseDao
    abstract fun muscleDao(): MuscleDao
    abstract fun trainingSetDao(): TrainingSetDao
    abstract fun trainingExerciseDao(): TrainingExerciseDao
    abstract fun trainingWorkoutDao(): TrainingWorkoutDao

    class ExerciseCategoryDatabaseCallback( // todo separate these out
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            Log.d("db creation", "exercise oncreate")
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.exerciseDao(), database.categoryDao(), database.muscleDao(),
                    database.trainingSetDao(), database.trainingExerciseDao(), database.trainingWorkoutDao())
                }
            }
        }

        suspend fun populateDatabase(exerciseDao: ExerciseDao, categoryDao: CategoryDao,
                                     muscleDao: MuscleDao, trainingSetDao: TrainingSetDao,
                                     trainingExerciseDao: TrainingExerciseDao,
        trainingWorkoutDao: TrainingWorkoutDao) {
            // Delete all content here.
            exerciseDao.deleteAll()
            categoryDao.deleteAll()
            muscleDao.deleteAll()
            trainingSetDao.deleteAll()
            trainingExerciseDao.deleteAll()
            trainingWorkoutDao.deleteAll()


            Log.d("db creation", "create legs")
            val legs = Category(0, "Legs")
            val chest = Category(0, "Chest")
            val abs = Category(0, "Abs")
            categoryDao.insert(legs, chest, abs)

            val quads = Muscle(0, "Quadriceps")
            val hams = Muscle(0, "Hamstrings")
            val traps = Muscle(0, "Traps")
            val pec_major = Muscle(0, "Pectoralis major")
            val pec_minor = Muscle(0, "Pectoralis minor")
            val core = Muscle(0, "Core")
            muscleDao.insert(quads, hams, traps, pec_major, pec_minor)

            // Add sample exercises.
            val squat = Exercise(0,"Barbell squat",
                categoryDao.getCategoryByName("Legs"), quads, hams)
            val bench = Exercise(0,"Barbell bench press",
                categoryDao.getCategoryByName("Chest"), pec_major, pec_minor)
            val legRaise = Exercise(0,"Hanging leg raise",
                categoryDao.getCategoryByName("Abs"), core, core)
            val inclineDb = Exercise(0,"Incline dumbbell bench press",
                categoryDao.getCategoryByName("Chest"), pec_major, pec_minor)
            exerciseDao.insert(squat, bench, legRaise, inclineDb)

        }
    }

    companion object { // todo understand
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        var INSTANCE: MasterDatabase? = null

        fun getDatabase(context: Context, scope:CoroutineScope): MasterDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            Log.d("db creation", "fun get")
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MasterDatabase::class.java,
                    "training_database"
                )
                    .addCallback(ExerciseCategoryDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

}