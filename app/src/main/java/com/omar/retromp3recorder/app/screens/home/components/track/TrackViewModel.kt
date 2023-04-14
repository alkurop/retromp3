package com.omar.retromp3recorder.app.screens.home.components.track

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.omar.retromp3recorder.bl.system.TrackWatcherUC
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TrackViewModel @Inject constructor(
    trackWatcherUC: TrackWatcherUC,
) : ViewModel() {
    init {
        viewModelScope.launch {
            trackWatcherUC.execute(viewModelScope)
        }
    }

    fun bind() {
        //noop
    }
}
