package com.jeywoods.foodcalorieanalyzer.domain.usecase

import android.graphics.Bitmap
import com.jeywoods.foodcalorieanalyzer.domain.repository.PhotoRepository
import javax.inject.Inject

class SavePhotoUseCase @Inject constructor(
    private val photoRepository: PhotoRepository
) {
    suspend operator fun invoke(bitmap: Bitmap): String {
        return photoRepository.savePhoto(bitmap)
    }
}