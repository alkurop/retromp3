package com.omar.retromp3recorder.app.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.SharingStarted.Companion.Lazily
import kotlinx.coroutines.flow.SharingStarted.Companion.WhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn

fun <T> Flow<T>.stateInViewModel(viewModel: ViewModel, default: T): StateFlow<T> =
    this.stateIn(viewModel.viewModelScope, SharingStarted.Companion.Eagerly, default)
