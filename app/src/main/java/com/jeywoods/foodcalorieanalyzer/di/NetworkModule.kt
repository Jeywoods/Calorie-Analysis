package com.jeywoods.foodcalorieanalyzer.di

import android.util.Log
import com.jeywoods.foodcalorieanalyzer.data.remote.CalorieNinjasApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import com.jeywoods.foodcalorieanalyzer.BuildConfig

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        Log.d("NetworkModule", "API Key: '${BuildConfig.CALORIE_NINJAS_API_KEY}'")
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("X-Api-Key", BuildConfig.CALORIE_NINJAS_API_KEY)
                    .build()
                chain.proceed(request)
            }
            .addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            })
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl("https://api.calorieninjas.com/")
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideCalorieNinjasApi(retrofit: Retrofit): CalorieNinjasApi {
        return retrofit.create(CalorieNinjasApi::class.java)
    }
}