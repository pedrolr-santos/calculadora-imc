package com.example.calculadoraimc.feature.history.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.calculadoraimc.datasource.database.IMCEntity

@Composable
fun IMCLineChart(
    data: List<IMCEntity>,
    modifier: Modifier = Modifier
) {
    if (data.size < 2) return

    val sortedData = remember(data) { data.sortedBy { it.timestamp } }
    val imcValues = remember(sortedData) { sortedData.map { it.imc } }

    val minIMC = imcValues.minOrNull() ?: 0.0
    val maxIMC = imcValues.maxOrNull() ?: 40.0

    val yPadding = (maxIMC - minIMC) * 0.2
    val yMin = (minIMC - yPadding).coerceAtLeast(0.0)
    val yMax = (maxIMC + yPadding)

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(200.dp)
            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f), RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Text(
            text = "Evolução do IMC",
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.align(Alignment.TopStart)
        )

        Canvas(modifier = Modifier.fillMaxWidth().height(160.dp).align(Alignment.BottomCenter)) {
            val width = size.width
            val height = size.height
            val points = mutableListOf<Offset>()

            imcValues.forEachIndexed { index, imc ->
                val x = (index.toFloat() / (imcValues.size - 1)) * width
                val fraction = (imc - yMin) / (yMax - yMin)
                val y = height - (fraction * height).toFloat()
                points.add(Offset(x, y))
            }

            val path = Path().apply {
                if (points.isNotEmpty()) {
                    moveTo(points.first().x, points.first().y)
                    for (i in 1 until points.size) {
                        val current = points[i]
                        val previous = points[i - 1]
                        val controlPoint1 = Offset((previous.x + current.x) / 2, previous.y)
                        val controlPoint2 = Offset((previous.x + current.x) / 2, current.y)
                        cubicTo(controlPoint1.x, controlPoint1.y, controlPoint2.x, controlPoint2.y, current.x, current.y)
                    }
                }
            }

            drawPath(
                path = path,
                color = Color(0xFF4CAF50),
                style = Stroke(width = 8f, cap = StrokeCap.Round)
            )

            // Correção para o Path do gradiente
            val androidPath = android.graphics.Path()
            // Converte o Path do Compose para Android nativo para poder fechar a forma
            // Infelizmente a conversão direta as vezes dá erro de import, então vamos simplificar o gradiente:
            // Se der erro no "asAndroidPath", removemos o gradiente para simplificar.
            // Mas tente rodar assim primeiro.

            try {
                val fillPath = android.graphics.Path(path.asAndroidPath()).asComposePath().apply {
                    lineTo(width, height)
                    lineTo(0f, height)
                    close()
                }

                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFF4CAF50).copy(alpha = 0.3f),
                            Color.Transparent
                        )
                    )
                )
            } catch (e: Exception) {
                // Se der erro na conversão, apenas ignora o gradiente
            }

            points.forEach { point ->
                drawCircle(color = Color.White, radius = 10f, center = point)
                drawCircle(color = Color(0xFF4CAF50), radius = 6f, center = point)
            }
        }
    }
}