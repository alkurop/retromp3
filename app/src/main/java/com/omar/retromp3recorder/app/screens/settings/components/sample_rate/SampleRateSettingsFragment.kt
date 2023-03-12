package com.omar.retromp3recorder.app.screens.settings.components.sample_rate

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.jakewharton.rxbinding4.view.clicks
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.screens.settings.components.RecorderSettingsBaseFragment
import com.omar.retromp3recorder.app.utils.observe
import com.omar.retromp3recorder.iorecorder.Mp3VoiceRecorder

class SampleRateSettingsFragment : RecorderSettingsBaseFragment() {

    private val viewModel by viewModels<SampleRateViewModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addTitleView(getString(R.string.sample_rate))
        val group = Mp3VoiceRecorder.SampleRate.values().map { sampleRate ->
            addCheckBox(
                title = getString(
                    R.string.sample_rate_format,
                    sampleRate.value
                )
            )
        }
        group.mapIndexed { index, radioButton ->
            radioButton.clicks()
                .observe(viewLifecycleOwner) {
                    val sampleRate = Mp3VoiceRecorder.SampleRate.values()[index]
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
