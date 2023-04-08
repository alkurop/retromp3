package com.omar.retromp3recorder.ui.wavetable.compose

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PointMode
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.unit.dp
import com.omar.retromp3recorder.domain.Wavetable


@Immutable
data class WavetableComposeData(
    val wavetable: Wavetable
)


@Composable
fun WavetableCompose(
    data: WavetableComposeData,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val dataPointsTop = getDataPoints(size, data.wavetable.bytes)
        val dataPointsBottom = getDataPoints(size, data.wavetable.bytes, true)
        drawDataPoints(dataPointsTop)
        drawDataPoints(dataPointsBottom)
    }
}

private fun DrawScope.drawDataPoints(data: List<Offset>) {
    drawPoints(
        color = Color.Green,
        points = data,
        cap = StrokeCap.Round,
        pointMode = PointMode.Polygon,
        strokeWidth = 2f
    )
}

private fun getDataPoints(size: Size, data: ByteArray, isMinus: Boolean = false): List<Offset> {
    val list = mutableListOf<Offset>()
    val verticalCenter = size.height / 2

    val step = size.width / (data.size - 1)
    val max = Byte.MAX_VALUE

    data.forEachIndexed { index, byte ->
        val offset = byte / max.toFloat() * verticalCenter
        list.add(
            Offset(
                step * index.toFloat(),
                if (isMinus) verticalCenter - offset
                else verticalCenter + offset
            )
        )
    }
    return list
}
