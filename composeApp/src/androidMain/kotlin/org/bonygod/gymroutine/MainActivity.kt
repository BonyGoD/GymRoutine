package org.bonygod.gymroutine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.bonygod.gymroutine.ui.theme.CustomBlack
import org.bonygod.gymroutine.ui.theme.CustomLightGray
import org.bonygod.gymroutine.ui.theme.CustomYellow
import org.bonygod.gymroutine.ui.view.App
import org.bonygod.gymroutine.ui.view.components.CustomTextField
import org.bonygod.gymroutine.ui.view.home.composables.SaveButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {

            val isDarkTheme = isSystemInDarkTheme()
            val darkColor = Color.Transparent
            val lightColor = Color.Transparent

            enableEdgeToEdge(
                statusBarStyle = if (!isDarkTheme) {
                    SystemBarStyle.dark(darkColor.hashCode())
                } else SystemBarStyle.light(lightColor.hashCode(), lightColor.hashCode()),
                navigationBarStyle = if (!isDarkTheme) {
                    SystemBarStyle.dark(darkColor.hashCode())
                } else SystemBarStyle.light(lightColor.hashCode(), lightColor.hashCode())
            )
            App()
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun AppAndroidPreview() {
    val expanded = remember { mutableStateOf(false) }
    val selectores = remember { mutableStateListOf<String>() }
    val nombreEjercicio = remember { mutableStateListOf("", "", "", "") }
    val tipos = listOf("Ejercicio", "Series", "Repeticiones", "Descanso")

    selectores.add("Fuerza o hipertrofia") // MET 0.12–0.17 kcal/seg
    selectores.add("Alta Intensidad (HIIT)") // MET 0.17–0.25 kcal/seg
    selectores.add("Baja Intensidad (LISS)") // MET 0.08–0.13 kcal/seg
    selectores.add("Resistencia Muscular") // MET 0.13–0.17 kcal/seg
    selectores.add("Entrenamiento Funcional") // MET 0.13–0.21 kcal/seg

    //Formula gasto calorico
    //Calorias quemadas=Duracion(min)×(MET×3.5×Peso(kg))/200

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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            LazyColumn(
                modifier = Modifier
                    .weight(1f)
                    .padding(bottom = 16.dp)
            ) {
                items(tipos.size) { index ->
                    Spacer(modifier = Modifier.height(10.dp))
                    CustomTextField(
                        value = nombreEjercicio[index],
                        title = tipos[index],
                        checkEmail = false
                    ) {
                        nombreEjercicio[index] = it
                    }
                }
            }
            SaveButton(
                isButtonEnabled = true
            ) { }
        }
    }
}

