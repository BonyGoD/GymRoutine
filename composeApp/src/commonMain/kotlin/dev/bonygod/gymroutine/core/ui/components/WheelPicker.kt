package dev.bonygod.gymroutine.core.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlin.math.abs

/**
 * Carrete vertical tipo "máquina tragaperras" / selector de fecha de iOS: los valores se
 * desplazan en vertical, el que queda en el centro es el seleccionado, y al soltar el scroll
 * hace snap para que siempre quede un valor encajado en el centro.
 *
 * El contentPadding (arriba y abajo) equivale a la mitad de la altura visible, así el primer y
 * el último valor de [values] también pueden llegar a quedar centrados. No se añaden items de
 * relleno: eso descuadraría [values]-> índice.
 */
@Composable
fun WheelPicker(
    values: List<Int>,
    selectedValue: Int,
    onValueChange: (Int) -> Unit,
    modifier: Modifier = Modifier,
    visibleItems: Int = 5,
) {
    val colorScheme = MaterialTheme.colorScheme
    val itemHeight = 40.dp
    val listState = rememberLazyListState()
    val sidePadding = itemHeight * (visibleItems / 2)

    // El elemento centrado no es "el primero visible": con visibleItems = 5 el centrado
    // queda dos posiciones por debajo de firstVisibleItemIndex. En vez de asumir esa
    // aritmética (que se rompe si cambian itemHeight, visibleItems o el contentPadding),
    // se busca el elemento cuyo centro está más cerca del centro real del viewport.
    val centerIndex by remember {
        derivedStateOf {
            val info = listState.layoutInfo
            val viewportCenter = (info.viewportStartOffset + info.viewportEndOffset) / 2
            info.visibleItemsInfo.minByOrNull { abs((it.offset + it.size / 2) - viewportCenter) }?.index ?: 0
        }
    }

    LaunchedEffect(Unit) {
        val initialIndex = values.indexOf(selectedValue).coerceAtLeast(0)
        // scrollToItem posiciona el índice como primero visible, no como centrado: hay que
        // restarle la mitad de los elementos visibles para que el valor inicial quede en el centro.
        listState.scrollToItem((initialIndex - visibleItems / 2).coerceAtLeast(0))
    }

    LaunchedEffect(listState) {
        snapshotFlow { centerIndex }
            .distinctUntilChanged()
            .collect { index ->
                values.getOrNull(index)?.let(onValueChange)
            }
    }

    Box(
        modifier = modifier.height(itemHeight * visibleItems),
        contentAlignment = Alignment.Center,
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(itemHeight)
                .background(colorScheme.surfaceVariant, RoundedCornerShape(12.dp)),
        )

        LazyColumn(
            state = listState,
            flingBehavior = rememberSnapFlingBehavior(listState),
            contentPadding = PaddingValues(vertical = sidePadding),
            modifier = Modifier.fillMaxWidth(),
        ) {
            itemsIndexed(values, key = { _, value -> value }) { index, value ->
                val distance = abs(index - centerIndex)
                val isSelected = distance == 0
                val alpha = when (distance) {
                    0 -> 1f
                    1 -> 0.5f
                    else -> 0.25f
                }
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(itemHeight),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = value.toString(),
                        color = if (isSelected) colorScheme.onSurface else colorScheme.onSurfaceVariant,
                        fontSize = if (isSelected) 22.sp else 18.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                        modifier = Modifier.alpha(alpha),
                    )
                }
            }
        }
    }
}
