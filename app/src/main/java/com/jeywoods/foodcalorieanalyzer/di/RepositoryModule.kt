package com.jeywoods.foodcalorieanalyzer.di

import com.jeywoods.foodcalorieanalyzer.data.repository.FoodRepositoryImpl
import com.jeywoods.foodcalorieanalyzer.data.repository.MealRepositoryImpl
import com.jeywoods.foodcalorieanalyzer.data.repository.PhotoRepositoryImpl
import com.jeywoods.foodcalorieanalyzer.domain.repository.FoodRepository
import com.jeywoods.foodcalorieanalyzer.domain.repository.MealRepository
import com.jeywoods.foodcalorieanalyzer.domain.repository.PhotoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindFoodRepository(
        foodRepositoryImpl: FoodRepositoryImpl
    ): FoodRepository

    @Binds
    @Singleton
    abstract fun bindMealRepository(
        mealRepositoryImpl: MealRepositoryImpl
    ): MealRepository

    @Binds
    @Singleton
    abstract fun bindPhotoRepository(
        photoRepositoryImpl: PhotoRepositoryImpl
    ): PhotoRepository
}