package com.omar.retromp3recorder.app.ui.recorder_settings.waverate

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.jakewharton.rxbinding4.view.clicks
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.recorder_settings.RecorderSettingsBaseFragment
import com.omar.retromp3recorder.app.uiutils.observe
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder
import com.omar.retromp3recorder.utils.Constants.RECORDING_LENGTH_MULTIPLIER

class WaveSampleRateSettingsFragment : RecorderSettingsBaseFragment() {

    private val viewModel by viewModels<WaveSampleRateSettingsViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addTitleView(getString(R.string.wavetable_sample_rate))
        val group = Mp3VoiceRecorder.WaveTableSampleRate.values().map { sampleRate ->
            val sps = 1000 / sampleRate.value
            val maxSeconds = RECORDING_LENGTH_MULTIPLIER / sps
            val maxMinutes = maxSeconds / 60
            addCheckBox(
                title = getString(
                    R.string.wavetable_sample_rate_format,
                    sps, maxMinutes
                )
            )
        }
        group.mapIndexed { index, radioButton ->
            radioButton.clicks()
                .observe(viewLifecycleOwner) {
                    val sampleRate = Mp3VoiceRecorder.WaveTableSampleRate.values()[index]
                    viewModel.input.onNext(sampleRate)
                }
        }

        viewModel.state
            .observe(viewLifecycleOwner) { state ->
                group.forEachIndexed { index, radioButton ->
                    radioButton.isChecked = state.ordinal == index
                }
            }
    }
}
