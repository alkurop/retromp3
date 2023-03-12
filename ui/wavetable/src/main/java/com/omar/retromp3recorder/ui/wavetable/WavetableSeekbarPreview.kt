package com.omar.retromp3recorder.ui.wavetable

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.omar.retromp3recorder.domain.FromToMillis
import com.omar.retromp3recorder.domain.JoinedProgress
import com.omar.retromp3recorder.utils.toFromToMillis
import com.omar.retromp3recorder.utils.toPlayerTime
import com.omar.retromp3recorder.utils.toSeekbarTime
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.subjects.BehaviorSubject

class WavetableSeekbarPreview @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val wavetableProgressBar: WavetableProgressBar
        get() = findViewById(R.id.progress_bar)
    private val wavetablePreview: WavetablePreview
        get() = findViewById(R.id.wavetable_preview)
    private val seekbar: SeekBar
        get() = findViewById(R.id.seek_bar)
    private val isSeekingBus = BehaviorSubject.create<SeekState>()
    private val shouldUpdateProgressBar =
        isSeekingBus.hasValue().not() || isSeekingBus.blockingFirst() is SeekState.SeekFinished

    private var currentState: JoinedProgress.PlayerProgressShown? = null

    init {
        View.inflate(context, R.layout.view_wavetable_seekbar, this)

        seekbar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (fromUser) {
                    val seeking = SeekBarResult(
                        progress
                    ).convertToRealNumbers(currentState!!)
                    isSeekingBus.onNext(seeking)
                    val (p, d) = seeking
                    wavetableProgressBar.update(WaveTableProgress(FromToMillis(p, d), getRange()))
                }
            }

            override fun onStartTrackingTouch(v: SeekBar?) {
                isSeekingBus.onNext(SeekState.SeekStarted)
            }

            override fun onStopTrackingTouch(v: SeekBar?) {
                isSeekingBus.onNext(
                    SeekState.SeekFinished
                )
            }
        })
        seekbar.setPadding(0, 0, 0, 0)
    }

    fun observeIsSeeking(): Observable<SeekState> = isSeekingBus

    fun update(joinedProgress: JoinedProgress.PlayerProgressShown) {
        if (currentState == joinedProgress) return
        currentState = joinedProgress

        fun updateProgress(progress: FromToMillis, range: FromToMillis?) {
            if (shouldUpdateProgressBar) {
                wavetableProgressBar.update(WaveTableProgress(progress, range))
                seekbar.max = (progress.to.toSeekbarTime())
                seekbar.progress = (progress.from.toSeekbarTime())
            }
        }

        val progress = joinedProgress.progress
        updateProgress(
            FromToMillis(progress.progress, progress.duration),
            getRange()
        )

        val wavetable = joinedProgress.wavetable
        if (wavetable != null) {
            wavetablePreview.update(
                BytesWithRange(wavetable.data, joinedProgress.progress.range)
            )
        }
    }

    sealed class SeekState {
        object SeekStarted : SeekState()
        data class Seeking(val progress: Long, val max: Long) : SeekState()
        object SeekFinished : SeekState()
    }

    data class SeekBarResult(
        val progress: Int,
    )


    private fun SeekBarResult.convertToRealNumbers(joinedProgress: JoinedProgress.PlayerProgressShown): SeekState.Seeking {
        return SeekState.Seeking(
            this.progress.toPlayerTime(),
            joinedProgress.progress.duration
        )
    }

    private fun getRange(): FromToMillis? {
        val progress = currentState?.progress
        val range = progress?.range ?: return null
        return if (range.settings.isActive) {
            range.toFromToMillis(progress.duration)
        } else null
    }
}
