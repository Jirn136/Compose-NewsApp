import android.graphics.Paint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

@Composable
fun QualityLevelIndicator(pair: Pair<String, Float>, modifier: Modifier = Modifier) {
    val element = pair.first
    val qualityRange = pair.second

    Box(modifier = modifier.size(150.dp)) {
        val color = when (qualityRange) {
            1f -> Color.Green
            2f -> Color.Yellow
            3f -> Color(0xFFFFA500)
            4f -> Color.Red
            else -> Color(0xFF8B0000)

        }
        Canvas(modifier = modifier.matchParentSize(), onDraw = {
            val radius = size.minDimension / 2f
            val center = Offset(size.width / 2f, size.height / 2f)
            val startAngle = 0f
            val sweepAngle = 360f * qualityRange
            val textX =
                center.x + (radius * 0.8 * cos(Math.toRadians((startAngle + sweepAngle / 2).toDouble()))).toFloat()
            val textY =
                center.y + (radius * 0.8 * sin(Math.toRadians((startAngle + sweepAngle / 2).toDouble()))).toFloat()

            drawIntoCanvas {
                drawArc(
                    color = color,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle,
                    useCenter = true,
                    size = Size(radius * 2, radius * 2)
                )
                drawIntoCanvas {
                    it.nativeCanvas.drawText(element, textX, textY, Paint().apply {
                        textAlign = Paint.Align.CENTER
                        setColor(Color.Black.toArgb())
                        isAntiAlias = true
                        textSize = 20f
                    })
                }
            }


        })
    }
}