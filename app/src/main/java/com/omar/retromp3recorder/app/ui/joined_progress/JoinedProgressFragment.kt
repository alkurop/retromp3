package com.omar.retromp3recorder.app.ui.joined_progress

import android.os.Bundle
import android.view.View
import androidx.core.view.isGone
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.utils.lazyView
import com.omar.retromp3recorder.app.uiutils.observe
import com.omar.retromp3recorder.dto.JoinedProgress
import com.omar.retromp3recorder.ui.wavetable.BytesWithRange
import com.omar.retromp3recorder.ui.wavetable.WavetablePreview
import com.omar.retromp3recorder.ui.wavetable.WavetableSeekbarPreview
import com.omar.retromp3recorder.utils.toSeekbarTime

class JoinedProgressFragment : Fragment(R.layout.fragment_joined_progress) {
    private val viewModel by viewModels<JoinedProgressViewModel>()
    private val recorderWavetable by lazyView<WavetablePreview>(R.id.fjp_recorder_wavetable)
    private val noFileMessage by lazyView<View>(R.id.no_file)
    private val playerProgress by lazyView<WavetableSeekbarPreview>(R.id.fjp_player_progress)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::renderView)
        playerProgress.observeIsSeeking().observe(viewLifecycleOwner) {
            val event = when (it) {
                is WavetableSeekbarPreview.SeekState.Seeking ->
                    JoinedProgressView.In.SeekToPosition(it.progress)
                is WavetableSeekbarPreview.SeekState.SeekStarted -> {
                    JoinedProgressView.In.SeekingStarted
                }
                is WavetableSeekbarPreview.SeekState.SeekFinished -> {
                    JoinedProgressView.In.SeekingFinished
                }
            }
            viewModel.input.onNext(event)
        }
    }

    private fun renderView(joinedProgress: JoinedProgress) {
        playerProgress.isGone =
            joinedProgress !is JoinedProgress.PlayerProgressShown && joinedProgress !is JoinedProgress.Intermediate
        recorderWavetable.isGone = joinedProgress !is JoinedProgress.RecorderProgressShown
        noFileMessage.isGone = joinedProgress !is JoinedProgress.Hidden

        when (joinedProgress) {
            is JoinedProgress.RecorderProgressShown -> recorderWavetable.update(
                BytesWithRange(joinedProgress.wavetable.data, null)
            )
            is JoinedProgress.PlayerProgressShown -> {
                val progress = joinedProgress.progress
                playerProgress.updateProgress(
                    progress.progress.toSeekbarTime() to progress.duration.toSeekbarTime()
                )

                val wavetable = joinedProgress.wavetable
                if (wavetable != null) {
                    playerProgress.updateWavetable(
                        BytesWithRange(wavetable.data, joinedProgress.progress.range)
                    )
                }
            }
            else -> {}
        }
    }
}
