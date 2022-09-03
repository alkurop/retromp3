package com.omar.retromp3recorder.app.ui.audio_controls

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.isInvisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.jakewharton.rxbinding4.view.clicks
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.utils.lazyView
import com.omar.retromp3recorder.app.uiutils.observe
import com.omar.retromp3recorder.ui.state_button.InteractiveButton
import com.omar.retromp3recorder.ui.state_button.InteractiveButtonBlinking
import com.omar.retromp3recorder.utils.toDisplay
import com.omar.retromp3recorder.utils.toSpannableStringWithSmallMillis
import io.reactivex.rxjava3.core.Observable.merge

class AudioControlsFragment : Fragment(R.layout.fragment_audio_controls) {
    private val playButton: InteractiveButtonBlinking by lazyView(R.id.acf_play)
    private val recordButton by lazyView<InteractiveButton>(R.id.acf_record)
    private val shareButton by lazyView<InteractiveButton>(R.id.acf_share)
    private val stopButton by lazyView<InteractiveButton>(R.id.acf_stop)
    private val progressView by lazyView<TextView>(R.id.acf_player_progress)
    private val durationView by lazyView<TextView>(R.id.acf_player_duration)
    private val viewModel: AudioControlsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        merge(
            listOf(
                playButton.clicks().map { AudioControlsView.Input.Play },
                recordButton.clicks().map { AudioControlsView.Input.Record },
                stopButton.clicks().map { AudioControlsView.Input.Stop },
                shareButton.clicks().map { AudioControlsView.Input.Share }
            ))
            .observe(viewLifecycleOwner) { viewModel.input.onNext(it) }
        viewModel.state.observe(viewLifecycleOwner, ::renderState)
    }

    private fun renderState(state: AudioControlsView.State) {
        playButton.onState(state.playButtonState)
        recordButton.onState(state.recordButtonState)
        shareButton.onState(state.shareButtonState)
        stopButton.onState(state.stopButtonState)
        progressView.isInvisible = state.playerProgressState == null
        durationView.isInvisible = state.playerProgressState == null && state.recordingDuration == null
        state.playerProgressState?.apply {
            progressView.text = progress.toDisplay()
                .toSpannableStringWithSmallMillis(requireContext(), R.style.Control_Normal_Millis)
            durationView.text = duration.toDisplay()
                .toSpannableStringWithSmallMillis(requireContext(), R.style.Control_Normal_Millis)
        }
        state.recordingDuration?.let {
            durationView.text = it.toDisplay()
                .toSpannableStringWithSmallMillis(requireContext(), R.style.Control_Normal_Millis)
        }

    }
}