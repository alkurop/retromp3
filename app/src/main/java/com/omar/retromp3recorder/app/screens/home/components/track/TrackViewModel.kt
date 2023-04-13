package com.omar.retromp3recorder.app.screens.home.components.track

import androidx.lifecycle.ViewModel
import com.omar.retromp3recorder.bl.system.TrackWatcherUC
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TrackViewModel @Inject constructor(
    trackWatcherUC: TrackWatcherUC
) : ViewModel() {

    init {
        trackWatcherUC.execute()
    }

    fun bind() {
        //noop
    }
}
