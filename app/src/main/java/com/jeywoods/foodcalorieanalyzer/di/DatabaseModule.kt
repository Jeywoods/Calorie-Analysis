package com.jeywoods.foodcalorieanalyzer.di

import android.content.Context
import androidx.room.Room
import com.jeywoods.foodcalorieanalyzer.data.local.dao.ClassificationCacheDao
import com.jeywoods.foodcalorieanalyzer.data.local.dao.MealDao
import com.jeywoods.foodcalorieanalyzer.data.local.database.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "food_analyzer.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideMealDao(database: AppDatabase): MealDao {
        return database.mealDao()
    }

    @Provides
    fun provideClassificationCacheDao(database: AppDatabase): ClassificationCacheDao {
        return database.classificationCacheDao()
    }
}