package com.omar.retromp3recorder.app.screens.settings

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.scan

object SettingsViewOutputMapper {
    fun Flow<SettingsContract.Output>.mapOutputToStateFlow(): Flow<SettingsContract.State> {
        return this.scan(SettingsContract.State()) { oldState, output ->
            when (output) {
                is SettingsContract.Output.FlagsCollectionUpdate -> oldState.copy(
                    featureFlagsCollection = output.featureFlagsCollection
                )
            }
        }.distinctUntilChanged()
    }
}
