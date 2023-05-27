package com.omar.retromp3recorder.ui.wave_visualiser

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import kotlinx.coroutines.flow.MutableStateFlow

class VisualizerDisplayView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var points: FloatArray = FloatArray(0)
    private val rect = Rect()
    private val mForePaint = Paint()
    private val inputFlow = MutableStateFlow(fillByteArray())

    init {
        mForePaint.strokeWidth = 1f
        mForePaint.isAntiAlias = true
        mForePaint.color = Color.rgb(0, 255, 0)
    }

    fun updateVisualizer(bytes: ByteArray) {
        inputFlow.tryEmit(bytes)
        invalidate()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val mBytes = inputFlow.value
        if (points.size != mBytes.size * 4) {
            points = FloatArray(mBytes.size * 4)
        }
        rect.set(0, 0, width, height)
        val length = mBytes.size - 1
        val height = rect.height()


        for (i in 0 until length) {
            val index = i * 4
            points[index] = (rect.width() * i / length).toFloat()
            points[index + 2] = (rect.width() * (i + 1) / length).toFloat()
            points[index + 1] =
                height / 2 + ((mBytes[i]) * (height / 2) / Byte.MAX_VALUE).toFloat()
            points[index + 3] =
                height / 2 + (mBytes[i + 1] * (height / 2) / Byte.MAX_VALUE).toFloat()
        }
        canvas.drawLines(points, mForePaint)
    }
}

fun fillByteArray(): ByteArray {
    val bytes = mutableListOf<Byte>()
    val r = 0
    for (i in 1..1024) {
        bytes.add(r.toByte())
    }
    return bytes.toByteArray()
}
