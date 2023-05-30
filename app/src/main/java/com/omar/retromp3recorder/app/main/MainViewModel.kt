package com.omar.retromp3recorder.app.main

import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.app.utils.stateInViewModel
import com.omar.retromp3recorder.domain.FeatureFlag
import com.omar.retromp3recorder.utils.domain.toShell
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.scan
import javax.inject.Inject

/**
 *
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    interactor: MainActivityInteractor
) : ViewModel() {
    val state = interactor.processIO()
        .mapToState()
        .stateInViewModel(this, MainActivityContract.State())
}


private fun Flow<MainActivityContract.Output>.mapToState(): Flow<MainActivityContract.State> {
    return this.scan(MainActivityContract.State()) { oldState, output ->
        when (output) {
            is MainActivityContract.Output.ShowToast -> {
                oldState.copy(toast = output.text.toShell())
            }

            is MainActivityContract.Output.SettingsUpdated -> {
                val shouldKeepScreenOn =
                    output.featureFlagsCollection.isEnabled(FeatureFlag.KeepScreenOn)
                oldState.copy(
                    shouldKeepScreenOn = shouldKeepScreenOn
                )
            }
        }
    }
}
