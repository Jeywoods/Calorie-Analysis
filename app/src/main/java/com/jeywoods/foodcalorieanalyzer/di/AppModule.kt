package com.jeywoods.foodcalorieanalyzer.di

import android.content.Context
import com.jeywoods.foodcalorieanalyzer.data.ml.TFLiteFoodClassifier
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideTFLiteClassifier(@ApplicationContext context: Context): TFLiteFoodClassifier {
        return TFLiteFoodClassifier(context)
    }
}