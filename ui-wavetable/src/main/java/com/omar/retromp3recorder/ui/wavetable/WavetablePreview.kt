package com.omar.retromp3recorder.ui.wavetable

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import com.omar.retromp3recorder.dto.PlayerRange
import io.reactivex.rxjava3.subjects.BehaviorSubject
import kotlin.math.max

class WavetablePreview @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val paint: Paint = Paint().apply {
        strokeWidth = 1f
        style = Paint.Style.STROKE
        color = Color.rgb(0, 255, 0)
    }

    private val paintRange: Paint = Paint().apply {
        strokeWidth = 1f
        style = Paint.Style.STROKE
        color = Color.rgb(255, 191, 0)
    }

    private val bytesWithRangeBus = BehaviorSubject.create<BytesWithRange>()

    fun update(bytesWithRange: BytesWithRange) {
        bytesWithRangeBus.onNext(bytesWithRange)
        invalidate()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (bytesWithRangeBus.hasValue().not()) {
            return
        }
        val data = bytesWithRangeBus.blockingFirst()
        val mBytes = data.bytes
        val length = mBytes.size - 1
        val path = Path()
        val rangePath = Path()

        for (i in 0 until length) {
            val relativeLoudness = max(((mBytes[i]) * (height / 2) / Byte.MAX_VALUE), 1)
            val start = width * (i - 1) / length
            val end = width * (i) / length
            val rect = RectF(
                start.toFloat(),
                (height / 2 - relativeLoudness).toFloat().coerceAtLeast(0f),
                end.toFloat(),
                (height / 2 + relativeLoudness).toFloat(),
            )
            path.addRect(rect, Path.Direction.CCW)
        }
        canvas.drawPath(path, paint)

        val range = data.range
        if (range != null) {
            val max = range.max
            val rangeStart = RectF(
                (width * (range.from) / max).toFloat() - 1,
                0f,
                (width * (range.from) / max).toFloat(),
                height.toFloat(),
            )
            val rangeEnd = RectF(
                (width * (range.to) / max).toFloat() - 1,
                0f,
                (width * (range.to) / max).toFloat(),
                height.toFloat(),
            )
            rangePath.addRect(rangeStart, Path.Direction.CCW)
            rangePath.addRect(rangeEnd, Path.Direction.CCW)
            canvas.drawPath(rangePath, paintRange)
        }
    }
}

data class BytesWithRange(val bytes: ByteArray, val range: PlayerRange?) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as BytesWithRange

        if (!bytes.contentEquals(other.bytes)) return false
        if (range != other.range) return false

        return true
    }

    override fun hashCode(): Int {
        var result = bytes.contentHashCode()
        result = 31 * result + range.hashCode()
        return result
    }
}
