import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun QualityLevelIndicator(pair: Pair<String, Float>, modifier: Modifier = Modifier) {
    val element = pair.first.uppercase()
    val qualityRange = pair.second

    Box(
        modifier = modifier
            .size(60.dp)
    ) {
        val color = when (qualityRange) {
            1f -> Color.Green
            2f -> Color.Yellow
            3f -> Color(0xFFFFA500)
            4f -> Color.Red
            else -> Color(0xFF8B0000)
        }
        Canvas(
            modifier = Modifier.matchParentSize(),
            onDraw = {
                val diameter = size.minDimension
                val strokeWidth = 10f
                val radius = (diameter - strokeWidth) / 3
                val centerX = size.width / 2
                val centerY = size.height / 2

                // Draw colored circle
                drawCircle(
                    color = color,
                    center = Offset(centerX, centerY),
                    radius = radius,
                    style = Stroke(strokeWidth)
                )

                // Text properties
                val textPaint = Paint().asFrameworkPaint().apply {
                    textAlign = android.graphics.Paint.Align.CENTER
                    setColor(Color.Black.toArgb())
                    isAntiAlias = true
                    textSize = 30f
                }

                // Calculate text bounds
                val textBounds = android.graphics.Rect()
                textPaint.getTextBounds(element, 0, element.length, textBounds)

                // Calculate text position
                val textY = centerY + textBounds.height() / 2

                // Draw text at the center of the circle
                drawContext.canvas.nativeCanvas.drawText(element, centerX, textY, textPaint)
            }
        )
    }

}

@Preview(showBackground = true)
@Composable
fun PreviewCircularLevelProgressbar() {
    QualityLevelIndicator(
        Pair("so2", 3f), modifier = Modifier
    )
}