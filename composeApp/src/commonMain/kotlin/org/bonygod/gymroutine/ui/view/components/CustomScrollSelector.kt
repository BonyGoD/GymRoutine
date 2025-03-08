package org.bonygod.gymroutine.ui.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.boundsInParent
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.bonygod.gymroutine.ui.theme.CustomYellow

@Composable
fun CustomScrollSelector(initalValue: Int, firstElement: Int, lastElement: Int, onRoundSelected: (Int) -> Unit) {
    val rounds = (firstElement..lastElement).toList()
    val listState = rememberLazyListState()
    val parentWidth = remember { mutableStateOf(0) }
    var centerIndex by remember { mutableStateOf(rounds.indexOf(initalValue)) }

    LaunchedEffect(Unit) {
        val startIndex = rounds.indexOf(initalValue).coerceIn(0, rounds.size - 1)
        listState.scrollToItem(startIndex)
    }

    LaunchedEffect(listState) {
        snapshotFlow { listState.isScrollInProgress }
            .collect { isScrolling ->
                if (!isScrolling) {
                    val viewportCenter = parentWidth.value / 2
                    val centerItem = listState.layoutInfo.visibleItemsInfo.minByOrNull { item ->
                        kotlin.math.abs((item.offset + item.size / 2) - viewportCenter)
                    }

                    centerItem?.let {
                        centerIndex = it.index
                        listState.animateScrollToItem(it.index, -it.offset)
                        onRoundSelected(rounds[it.index])
                    }
                }
            }
    }

    LazyRow(
        state = listState,
        modifier = Modifier
            .width(300.dp)
            .height(100.dp)
            .onGloballyPositioned { coordinates ->
                parentWidth.value = coordinates.size.width
            },
        horizontalArrangement = Arrangement.Center
    ) {
        itemsIndexed(rounds) { _, round ->
            val isCenter = remember { mutableStateOf(false) }

            Box(
                modifier = Modifier
                    .padding(horizontal = 8.dp, vertical = 16.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(if (isCenter.value) CustomYellow else Color.Transparent)
                    .clickable { }
                    .padding(vertical = 12.dp, horizontal = 16.dp)
                    .onGloballyPositioned { coordinates ->
                        val itemCenter = coordinates.boundsInParent().center.x.toInt()
                        isCenter.value =
                            itemCenter in (parentWidth.value / 2 - 100)..(parentWidth.value / 2 + 100)
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = round.toString(),
                    color = if (isCenter.value) Color.Black else Color.Gray,
                    fontSize = if (isCenter.value) 28.sp else 15.sp,
                    fontWeight = if (isCenter.value) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}