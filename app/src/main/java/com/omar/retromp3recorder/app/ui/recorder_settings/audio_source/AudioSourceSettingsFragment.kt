package com.omar.retromp3recorder.app.ui.recorder_settings.audio_source

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.jakewharton.rxbinding4.view.clicks
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.recorder_settings.RecorderSettingsBaseFragment
import com.omar.retromp3recorder.app.uiutils.observe
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder

class AudioSourceSettingsFragment : RecorderSettingsBaseFragment() {

    private val viewModel by viewModels<AudioSourceViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addTitleView(getString(R.string.audio_source))
        val group = Mp3VoiceRecorder.AudioSourcePref.values().map { audioSource ->
            addCheckBox(
                title = getString(
                    audioSource.title
                )
            )
        }
        group.mapIndexed { index, radioButton ->
            radioButton.clicks()
                .observe(viewLifecycleOwner) {
                    val audioSource = Mp3VoiceRecorder.AudioSourcePref.values()[index]
                    viewModel.input.onNext(audioSource)
                }
        }

        viewModel.state
            .observe(viewLifecycleOwner) { state ->
                group.forEachIndexed { index, radioButton ->
                    radioButton.isChecked = state.ordinal == index
                    val canSwitch = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q
                    radioButton.isClickable = canSwitch
                    radioButton.alpha = if(canSwitch) 1f else 0.5f
                }
            }
    }
}