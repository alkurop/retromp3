package com.omar.retromp3recorder.ui.wavetable

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.SeekBar
import androidx.constraintlayout.widget.ConstraintLayout
import com.omar.retromp3recorder.domain.FromToMillis
import com.omar.retromp3recorder.domain.JoinedProgress
import com.omar.retromp3recorder.utils.domain.toFromToMillis
import com.omar.retromp3recorder.utils.platform.toPlayerTime
import com.omar.retromp3recorder.utils.platform.toSeekbarTime
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

    private val isSeekingBus = BehaviorSubject.create<SeekEvent>()

    private val shouldUpdateProgressBar =
        isSeekingBus.hasValue().not() || isSeekingBus.blockingFirst() is SeekEvent.SeekFinished

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
                isSeekingBus.onNext(SeekEvent.SeekStarted)
            }

            override fun onStopTrackingTouch(v: SeekBar?) {
                isSeekingBus.onNext(
                    SeekEvent.SeekFinished
                )
            }
        })
        seekbar.setPadding(0, 0, 0, 0)
    }

    fun observeIsSeeking(): Observable<SeekEvent> = isSeekingBus

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
                BytesWithRange(wavetable.bytes, joinedProgress.progress.range)
            )
        }
    }

    sealed class SeekEvent {
        object SeekStarted : SeekEvent()
        data class Seeking(val progress: Long, val max: Long) : SeekEvent()
        object SeekFinished : SeekEvent()
    }

    data class SeekBarResult(
        val progress: Int,
    )

    private fun SeekBarResult.convertToRealNumbers(joinedProgress: JoinedProgress.PlayerProgressShown): SeekEvent.Seeking {
        return SeekEvent.Seeking(
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
