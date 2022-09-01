package com.omar.retromp3recorder.ui.wavetable

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
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
    private val bytesBus = BehaviorSubject.create<ByteArray>()

    fun update(bytes: ByteArray) {
        bytesBus.onNext(bytes)
        invalidate()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        if (bytesBus.hasValue().not()) {
            return
        }
        val mBytes = bytesBus.blockingFirst()
        val length = mBytes.size - 1
        val path = Path()

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
    }
}
