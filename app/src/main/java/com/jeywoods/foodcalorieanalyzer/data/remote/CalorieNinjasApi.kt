package com.jeywoods.foodcalorieanalyzer.data.remote

import com.jeywoods.foodcalorieanalyzer.data.remote.dto.CalorieNinjasResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface CalorieNinjasApi {
    @GET("v1/nutrition")
    suspend fun getNutrition(
        @Query("query") query: String
    ): CalorieNinjasResponse
}