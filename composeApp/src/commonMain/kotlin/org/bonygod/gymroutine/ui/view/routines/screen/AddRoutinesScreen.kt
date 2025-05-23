package org.bonygod.gymroutine.ui.view.routines.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.bonygod.gymroutine.ui.theme.CustomBlack
import org.bonygod.gymroutine.ui.theme.CustomLightGray
import org.bonygod.gymroutine.ui.theme.CustomYellow
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun AddRoutinesScreen() {
    val expanded = remember { mutableStateOf(false) }
    val selectores = remember { mutableStateListOf<String>() }

    selectores.add("Selector 1")
    selectores.add("Selector 2")
    selectores.add("Selector 3")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CustomLightGray)
            .padding(top = 60.dp),
        horizontalAlignment = CenterHorizontally
    ) {
        Text(
            text = "Añade tu rutina",
            fontSize = 30.sp,
            color = CustomBlack,
            fontWeight = FontWeight.Bold,
        )
        LazyColumn(
            modifier = Modifier
                .padding(top = 20.dp)
        ) {
            item {
                Button(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp)
                        .border(1.dp, CustomBlack, RoundedCornerShape(30.dp))
                        .clip(shape = RoundedCornerShape(30.dp))
                        .height(35.dp),
                    onClick = { expanded.value = !expanded.value },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = CustomYellow
                    )
                ) {
                    Text(
                        text = "Selecciona tipo",
                        fontWeight = FontWeight.Bold,
                        color = CustomBlack,
                        fontSize = 15.sp
                    )
                }
                AnimatedVisibility(
                    visible = expanded.value,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp)
                            .background(Color.White, RoundedCornerShape(8.dp))
                            .border(1.dp, CustomBlack, RoundedCornerShape(8.dp))
                    ) {
                        Column {
                            selectores.forEach { itemSelector ->
                                DropdownMenuItem(
                                    text = {
                                        Box(
                                            modifier = Modifier.fillMaxWidth(),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = itemSelector,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 15.sp
                                            )
                                        }
                                    },
                                    onClick = {
                                        expanded.value = false
                                    },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}