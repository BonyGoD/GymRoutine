package dev.bonygod.gymroutine.evolution.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import dev.bonygod.gymroutine.routines.domain.model.ExerciseProgress
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.evolution_chart_legend_reps
import gymroutine.composeapp.generated.resources.evolution_chart_legend_weight
import org.jetbrains.compose.resources.stringResource

private val ChartHeight = 220.dp

// Separación mínima entre puntos. Por debajo de esto las etiquetas de valor se solapan.
private val MinStepWidth = 44.dp
private val ChartVerticalPadding = 24.dp
private val LabelFontSize = 10.sp
private val LabelGap = 4.dp
private const val GRID_LINE_COUNT = 4

/**
 * Gráfica de líneas de peso/reps a través del historial de un ejercicio, dibujada a mano con
 * [Canvas]: el catálogo de versiones no trae ninguna librería de gráficas.
 *
 * El eje X reparte los registros por índice, no por [ExerciseProgress.timestamp] — los registros
 * migrados de documentos antiguos llevan `timestamp = 0L` y distorsionarían la escala temporal.
 * Cada serie se normaliza a su propio rango vertical porque kilos y repeticiones no son comparables.
 */
@Composable
fun EvolutionChart(history: List<ExerciseProgress>, modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    val weightColor = colorScheme.primary
    val repsColor = colorScheme.tertiary
    val gridColor = colorScheme.outline.copy(alpha = 0.15f)
    // rememberTextMeasurer() es @Composable: hay que pedirlo aquí fuera, el Canvas no tiene contexto de composición.
    val textMeasurer = rememberTextMeasurer()
    val scrollState = rememberScrollState()

    // La gráfica arranca por el final: lo que interesa es el registro más reciente, y hacia los
    // antiguos se navega con scroll. `maxValue` solo se conoce tras medir, por eso es la clave del
    // efecto: salta una vez cuando el lienzo ya sabe cuánto se puede desplazar.
    LaunchedEffect(scrollState.maxValue) {
        scrollState.scrollTo(scrollState.maxValue)
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        // Ancho mínimo por tramo: con muchos registros la gráfica crece y se desplaza en horizontal
        // en vez de comprimirse hasta que los puntos y sus etiquetas se solapan.
        BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
            val chartWidth = maxOf(maxWidth, MinStepWidth * (history.size - 1).coerceAtLeast(1))
            Box(modifier = Modifier.horizontalScroll(scrollState)) {
                Canvas(
                    modifier = Modifier
                        .width(chartWidth)
                        .height(ChartHeight),
                ) {
                    // Rejilla: GRID_LINE_COUNT líneas horizontales repartidas de borde a borde.
                    repeat(GRID_LINE_COUNT) { line ->
                        val y = size.height * line / (GRID_LINE_COUNT - 1)
                        drawLine(
                            color = gridColor,
                            start = Offset(0f, y),
                            end = Offset(size.width, y),
                            strokeWidth = 1.dp.toPx(),
                        )
                    }

                    val verticalPaddingPx = ChartVerticalPadding.toPx()
                    drawSeries(
                        values = history.map { it.weight },
                        color = weightColor,
                        verticalPaddingPx = verticalPaddingPx,
                        textMeasurer = textMeasurer,
                        labelsAbove = true,
                        formatLabel = { it.toString() },
                    )
                    drawSeries(
                        values = history.map { it.reps.toFloat() },
                        color = repsColor,
                        verticalPaddingPx = verticalPaddingPx,
                        textMeasurer = textMeasurer,
                        labelsAbove = false,
                        formatLabel = { it.toInt().toString() },
                    )
                }
            }
        }

        Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
            ChartLegendRow(color = weightColor, text = stringResource(Res.string.evolution_chart_legend_weight))
            ChartLegendRow(color = repsColor, text = stringResource(Res.string.evolution_chart_legend_reps))
        }
    }
}

/**
 * Dibuja una serie normalizada a su propio rango vertical, con [verticalPaddingPx] de margen arriba
 * y abajo para que quepan las etiquetas de valor de [drawValueLabels]. El punto más alto/bajo queda
 * en el borde del margen, no del lienzo. Casos límite cubiertos explícitamente:
 * - Un solo valor: se pinta el punto sin línea (no hay `size - 1` con el que dividir por índice).
 * - Rango cero (`max == min`): la serie se centra verticalmente en vez de dividir entre cero.
 */
private fun DrawScope.drawSeries(
    values: List<Float>,
    color: Color,
    verticalPaddingPx: Float,
    textMeasurer: TextMeasurer,
    labelsAbove: Boolean,
    formatLabel: (Float) -> String,
) {
    if (values.isEmpty()) return
    val strokeWidthPx = 2.dp.toPx()
    val pointRadiusPx = 4.dp.toPx()

    val min = values.min()
    val max = values.max()
    val stepX = if (values.size > 1) size.width / (values.size - 1) else 0f
    val plotHeight = (size.height - verticalPaddingPx * 2).coerceAtLeast(0f)

    val points = values.mapIndexed { index, value ->
        val normalized = if (max == min) 0.5f else (value - min) / (max - min)
        Offset(x = stepX * index, y = verticalPaddingPx + plotHeight * (1f - normalized))
    }

    for (i in 0 until points.size - 1) {
        drawLine(color = color, start = points[i], end = points[i + 1], strokeWidth = strokeWidthPx)
    }
    points.forEach { point -> drawCircle(color = color, radius = pointRadiusPx, center = point) }

    drawValueLabels(
        points = points,
        values = values,
        color = color,
        textMeasurer = textMeasurer,
        labelsAbove = labelsAbove,
        formatLabel = formatLabel,
    )
}

/**
 * Dibuja el valor de cada punto, centrado horizontalmente sobre él y recortado a los bordes del
 * lienzo con [coerceIn] — el primer/último punto (extremo en X) y el punto más alto/bajo (extremo
 * en Y) son los que de otro modo sacarían la etiqueta fuera del `Canvas`.
 */
private fun DrawScope.drawValueLabels(
    points: List<Offset>,
    values: List<Float>,
    color: Color,
    textMeasurer: TextMeasurer,
    labelsAbove: Boolean,
    formatLabel: (Float) -> String,
) {
    val style = TextStyle(fontSize = LabelFontSize, color = color)
    val gapPx = LabelGap.toPx()

    points.forEachIndexed { index, point ->
        val text = formatLabel(values[index])
        val measured = textMeasurer.measure(text, style)
        val labelWidth = measured.size.width.toFloat()
        val labelHeight = measured.size.height.toFloat()

        val x = (point.x - labelWidth / 2f).coerceIn(0f, (size.width - labelWidth).coerceAtLeast(0f))
        val rawY = if (labelsAbove) point.y - gapPx - labelHeight else point.y + gapPx
        val y = rawY.coerceIn(0f, (size.height - labelHeight).coerceAtLeast(0f))

        drawText(textMeasurer = textMeasurer, text = text, topLeft = Offset(x, y), style = style)
    }
}

@Composable
private fun ChartLegendRow(color: Color, text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Box(
            modifier = Modifier
                .size(8.dp)
                .clip(CircleShape)
                .background(color),
        )
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 12.sp,
        )
    }
}
