package com.omar.retromp3recorder.storage.repo.global

import android.content.SharedPreferences
import com.omar.retromp3recorder.utils.domain.AudioCoroutineContext
import com.omar.retromp3recorder.utils.domain.repo.StateFlowRepo
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CropProductRepo @Inject constructor(
    private val audioCoroutineContext: AudioCoroutineContext,
    private val sharedPreferences: SharedPreferences
) : StateFlowRepo<Int>(0) {
    init {
        audioCoroutineContext.launch {
            val currentCropValue = sharedPreferences.getInt(
                KEY_CROP_COUNTER,
                INITIAL_CROP_OFFERING
            )
            emit(currentCropValue)
        }
    }

    override suspend fun emit(input: Int) {
        audioCoroutineContext.launch {
            val currentValue = sharedPreferences.getInt(
                KEY_CROP_COUNTER,
                INITIAL_CROP_OFFERING
            )
            sharedPreferences.edit().putInt(
                KEY_CROP_COUNTER,
                input
            ).apply()
            if (currentValue == 0) {
                sharedPreferences.edit().putBoolean(
                    KEY_CROP_COUNTER_TRIAL,
                    false
                ).apply()
            }
        }
        super.emit(input)
    }

    suspend fun isTrial(): Boolean {
        return withContext(audioCoroutineContext.coroutineContext) {
            sharedPreferences.getBoolean(KEY_CROP_COUNTER_TRIAL, true)
        }
    }
}

private const val KEY_CROP_COUNTER = "KEY_CROP_COUNTER"
private const val KEY_CROP_COUNTER_TRIAL = "KEY_CROP_COUNTER_TRIAL"
private const val INITIAL_CROP_OFFERING = 5
