package com.omar.retromp3recorder.app.ui.recorder_settings.beat_rate

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.jakewharton.rxbinding4.view.clicks
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.recorder_settings.RecorderSettingsBaseFragment
import com.omar.retromp3recorder.app.uiutils.observe
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder

class
BitRateSettingsFragment : RecorderSettingsBaseFragment() {
    private val viewModel by viewModels<BitRateSettingsViewModel>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addTitleView(getString(R.string.bit_rate))
        val group = Mp3VoiceRecorder.BitRate.values().map { sampleRate ->
            addCheckBox(
                title = getString(
                    R.string.bit_rate_format,
                    sampleRate.value
                )
            )
        }
        group.mapIndexed { index, radioButton ->
            radioButton.clicks()
                .observe(viewLifecycleOwner) {
                    val bitRate = Mp3VoiceRecorder.BitRate.values()[index]
                    viewModel.input.onNext(bitRate)
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
