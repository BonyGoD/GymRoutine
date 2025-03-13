package org.bonygod.gymroutine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.bonygod.gymroutine.ui.theme.CustomLightGray
import org.bonygod.gymroutine.ui.theme.CustomTheme
import org.bonygod.gymroutine.ui.view.App

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
    val maxLength = 15
    val text = "Extension de triceps en polea"
    val truncatedText = text.take(maxLength) + "..."
    CustomTheme {
        LazyColumn(
            modifier = Modifier.fillMaxWidth().padding(top = 50.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.Start
        ) {
            item {
                Text("Fuerza", modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 5.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape)
                        .background(CustomLightGray)
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "Press de banca", modifier = Modifier.width(120.dp), maxLines = 1)
                    Text(text = "4 series", modifier = Modifier.weight(1f).padding(start = 20.dp))
                    Text(text = "10 rep", modifier = Modifier.weight(1f).padding(start = 20.dp))
                    Text(text = "60 seg", modifier = Modifier.weight(1f).padding(start = 20.dp))
                }
                Spacer(modifier = Modifier.padding(5.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape)
                        .background(CustomLightGray)
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = truncatedText, modifier = Modifier.width(120.dp), maxLines = 1)
                    Text(text = "4 series", modifier = Modifier.weight(1f).padding(start = 20.dp))
                    Text(text = "10 rep", modifier = Modifier.weight(1f).padding(start = 20.dp))
                    Text(text = "60 seg", modifier = Modifier.weight(1f).padding(start = 20.dp))
                }
                Spacer(modifier = Modifier.padding(5.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape)
                        .background(CustomLightGray)
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = truncatedText, modifier = Modifier.width(120.dp), maxLines = 1)
                    Text(text = "4 series", modifier = Modifier.weight(1f).padding(start = 20.dp))
                    Text(text = "10 rep", modifier = Modifier.weight(1f).padding(start = 20.dp))
                    Text(text = "60 seg", modifier = Modifier.weight(1f).padding(start = 20.dp))
                }
                Spacer(modifier = Modifier.padding(5.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape)
                        .background(CustomLightGray)
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = truncatedText, modifier = Modifier.width(120.dp), maxLines = 1)
                    Text(text = "4 series", modifier = Modifier.weight(1f).padding(start = 20.dp))
                    Text(text = "10 rep", modifier = Modifier.weight(1f).padding(start = 20.dp))
                    Text(text = "60 seg", modifier = Modifier.weight(1f).padding(start = 20.dp))
                }
                Spacer(modifier = Modifier.padding(5.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(CircleShape)
                        .background(CustomLightGray)
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = truncatedText, modifier = Modifier.width(120.dp), maxLines = 1)
                    Text(text = "4 series", modifier = Modifier.weight(1f).padding(start = 20.dp))
                    Text(text = "10 rep", modifier = Modifier.weight(1f).padding(start = 20.dp))
                    Text(text = "60 seg", modifier = Modifier.weight(1f).padding(start = 20.dp))
                }
            }
        }
        Box(modifier = Modifier.fillMaxSize()){
            FloatingActionButton(
                onClick = { /*TODO*/ },
                modifier = Modifier.align(Alignment.BottomEnd).padding(16.dp)) {
                Icon(Icons.Rounded.Add, contentDescription = "Add")
            }
        }
    }
}