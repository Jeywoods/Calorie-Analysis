package com.jeywoods.foodcalorieanalyzer.data.remote

import com.jeywoods.foodcalorieanalyzer.data.remote.dto.CalorieNinjasResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Query

interface CalorieNinjasApi {
    @Headers("X-Api-Key: vCp7DnTpexf5FNjXl7LaLg==R4VDYF70COZIphwl")  // ← Замените на ваш ключ
    @GET("v1/nutrition")
    suspend fun getNutrition(
        @Query("query") query: String
    ): CalorieNinjasResponse
}