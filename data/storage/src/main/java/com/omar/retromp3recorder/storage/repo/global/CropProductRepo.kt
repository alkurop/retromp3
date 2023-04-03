package com.omar.retromp3recorder.storage.repo.global

import android.content.SharedPreferences
import com.omar.retromp3recorder.utils.domain.ScopeJobWrapper
import com.omar.retromp3recorder.utils.domain.repo.StateFlowRepo
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class CropProductRepo @Inject constructor(
    private val scopeJobWrapper: ScopeJobWrapper,
    private val sharedPreferences: SharedPreferences
) : StateFlowRepo<Int>(0) {
    init {
        scopeJobWrapper.launch {
            val currentCropValue = sharedPreferences.getInt(
                KEY_CROP_COUNTER,
                INITIAL_CROP_OFFERING
            )
            emit(currentCropValue)
        }
    }

    override suspend fun emit(input: Int) {
        scopeJobWrapper.launch {
            sharedPreferences.edit().putInt(
                KEY_CROP_COUNTER,
                input
            ).apply()
        }
        super.emit(input)
    }
}

private const val KEY_CROP_COUNTER = "KEY_CROP_COUNTER"
private const val INITIAL_CROP_OFFERING = 5
