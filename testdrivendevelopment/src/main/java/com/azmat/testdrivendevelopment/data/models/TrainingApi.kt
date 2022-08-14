package com.azmat.testdrivendevelopment.data.models

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.POST

interface TrainingApi {

    @POST("exercises/1")
    suspend fun getExercise() : Response<TrainingExerciseResponse>

}