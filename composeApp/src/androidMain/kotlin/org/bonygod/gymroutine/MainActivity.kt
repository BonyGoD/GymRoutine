package org.bonygod.gymroutine

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.bonygod.gymroutine.ui.theme.CustomBlack
import org.bonygod.gymroutine.ui.theme.CustomGray
import org.bonygod.gymroutine.ui.theme.CustomLightGray
import org.bonygod.gymroutine.ui.view.App
import org.bonygod.gymroutine.ui.view.routine.composables.BodyExercise
import org.bonygod.gymroutine.ui.view.routine.composables.ExerciceTexts
import org.bonygod.gymroutine.ui.view.routine.composables.RestCronometer

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
    var expandedRowIndex by remember { mutableStateOf(-1) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = CustomLightGray)
            .padding(top = 60.dp),
    ) {
        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = "Sesion de hoy",
            fontSize = 30.sp,
            color = CustomBlack,
            fontWeight = FontWeight.Bold,
        )
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = CenterHorizontally,
        ) {
            Text(
                modifier = Modifier.padding(vertical = 20.dp, horizontal = 20.dp),
                text = "Fuerza",
                fontSize = 20.sp,
                color = CustomBlack,
                fontWeight = FontWeight.Bold,
            )
            LazyColumn(
                horizontalAlignment = CenterHorizontally,
            ) {
                items(1) { index ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 10.dp, horizontal = 5.dp)
                            .clip(CircleShape)
                            .background(CustomGray)
                            .padding(horizontal = 20.dp, vertical = 10.dp)
                            .clickable {
                                expandedRowIndex = if (expandedRowIndex == index) -1 else index
                            },
                        horizontalArrangement = Arrangement.End,
                    ) {
                        ExerciceTexts()
                    }
                    if (expandedRowIndex == index) {
                        BodyExercise("","")
                        RestCronometer(true, 60)
                    }

                }

            }
            Spacer(modifier = Modifier.padding(5.dp))
        }
    }
}
