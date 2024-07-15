package com.example.musicove.ui.components

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.Path
import androidx.compose.ui.graphics.vector.PathNode
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.example.musicove.util.audio.VisualizerData
import com.example.musicove.util.audio.VisualizerHelper
import kotlin.math.roundToInt

@Composable
fun StackedBarVisualizer(
    data: VisualizerData,
    barCount: Int,
    modifier: Modifier = Modifier,
    maxStackCount: Int = 32,
    shape: Shape = RoundedCornerShape(size = 8.dp),
    barColors: List<Color> = listOf(
        Color.Red, Color.Yellow, Color.Green
    ),
    stackedBarBackgroundColor: Color = Color.Gray
) {

    var size by remember { mutableStateOf(IntSize.Zero) }

    Row(modifier = modifier.onSizeChanged { size = it }) {

        val viewportHeight = size.height.toFloat()
        val viewportWidth = size.width.toFloat()
        val padding = LocalDensity.current.run { 1.dp.toPx() }

        val nodes = calculateStackBarPoints(
            resampled = data.resample(barCount),
            viewportHeight = viewportHeight,
            viewportWidth = viewportWidth,
            barCount = barCount,
            maxStackCount = maxStackCount,
            horizontalPadding = padding,
            verticalPadding = padding
        ).mapIndexed { index, point ->
            if (index % 4 == 0) {
                PathNode.MoveTo(point.x(), point.y())
            } else {
                PathNode.LineTo(point.x(), point.y())
            }
        }

        val vectorPainter = rememberVectorPainter(
            defaultWidth = viewportWidth.dp,
            defaultHeight = viewportHeight.dp,
            viewportWidth = viewportWidth,
            viewportHeight = viewportHeight,
            autoMirror = false
        ) { _, _ ->
            Path(
                pathData = nodes,
                fill = Brush.linearGradient(
                    colors = barColors,
                    start = Offset.Zero,
                    end = Offset(0f, Float.POSITIVE_INFINITY)
                )
            )
        }

        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(shape = shape)
        ) {
            StackedBarVisualizerBackground(
                barCount = barCount,
                maxStackCount = maxStackCount,
                stackedBarBackgroundColor = stackedBarBackgroundColor,
                viewportHeight = viewportHeight,
                viewportWidth = viewportWidth,
                horizontalPadding = padding,
                verticalPadding = padding
            )
            Image(
                painter = vectorPainter,
                contentDescription = ""
            )
        }
    }
}

@JvmInline
private value class Point(private val p: Pair<Float, Float>) {

    fun x() = p.first
    fun y() = p.second

    operator fun plus(other: Point) = Point(x() + other.x() to y() + other.y())
    operator fun minus(other: Point) = Point(x() - other.x() to y() - other.y())
    operator fun times(factor: Float) = Point(x() * factor to y() * factor)
    operator fun div(factor: Float) = times(1f / factor)
}

@Composable
private fun calculateStackBarPoints(
    resampled: IntArray,
    viewportHeight: Float,
    viewportWidth: Float,
    barCount: Int,
    maxStackCount: Int,
    horizontalPadding: Float,
    verticalPadding: Float
): List<Point> {

    val barWidth = (viewportWidth / barCount) - horizontalPadding
    val stackHeightWithPadding = viewportHeight / maxStackCount
    val stackHeight = stackHeightWithPadding - verticalPadding

    val nodes = mutableListOf<Point>()

    resampled.forEachIndexed { index, d ->
        val stackCount = animateIntAsState(
            targetValue = (maxStackCount * (d / 128f)).roundToInt(),
            animationSpec = tween(durationMillis = VisualizerHelper.SAMPLING_INTERVAL),
            label = ""
        )

        for (stackIndex in 0 until stackCount.value) {
            // 4 corner points of the small rectangle in the stack:
            // LeftBottom corner point
            nodes += Point(
                barWidth * index + horizontalPadding * index to
                        viewportHeight - stackIndex * stackHeight - stackIndex * verticalPadding
            )
            // RightBottom corner point
            nodes += Point(
                barWidth * (index + 1) + horizontalPadding * index to
                        viewportHeight - stackIndex * stackHeight - stackIndex * verticalPadding
            )
            // TopRight corner point
            nodes += Point(
                barWidth * (index + 1) + horizontalPadding * index to
                        viewportHeight - (stackIndex + 1) * stackHeight - stackIndex * verticalPadding
            )
            // TopLeft corner point
            nodes += Point(
                barWidth * index + horizontalPadding * index to
                        viewportHeight - (stackIndex + 1) * stackHeight - stackIndex * verticalPadding
            )
        }
    }
    return nodes
}

@Composable
private fun StackedBarVisualizerBackground(
    barCount: Int,
    maxStackCount: Int,
    stackedBarBackgroundColor: Color,
    viewportHeight: Float,
    viewportWidth: Float,
    horizontalPadding: Float,
    verticalPadding: Float
) {
    Row(modifier = Modifier.fillMaxSize()) {

        val nodes = calculateStackBarPoints(
            resampled = VisualizerData.getMaxProcessed(resolution = barCount),
            viewportHeight = viewportHeight,
            viewportWidth = viewportWidth,
            barCount = barCount,
            maxStackCount = maxStackCount,
            horizontalPadding = horizontalPadding,
            verticalPadding = verticalPadding
        ).mapIndexed { index, point ->
            if (index % 4 == 0) {
                PathNode.MoveTo(point.x(), point.y())
            } else {
                PathNode.LineTo(point.x(), point.y())
            }
        }

        val vectorPainter = rememberVectorPainter(
            defaultWidth = viewportWidth.dp,
            defaultHeight = viewportHeight.dp,
            viewportWidth = viewportWidth,
            viewportHeight = viewportHeight,
            autoMirror = false
        ) { _, _ ->
            Path(
                pathData = nodes,
                fill = SolidColor(stackedBarBackgroundColor)
            )
        }

        Image(
            painter = vectorPainter,
            contentDescription = ""
        )

    }
}