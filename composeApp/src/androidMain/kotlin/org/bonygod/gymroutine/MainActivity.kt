package org.bonygod.gymroutine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import org.bonygod.gymroutine.ui.navigation.Tabs
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
fun AppAndroidPreview(onItemClicked: (Tabs) -> Unit) {
//    var tabIndex by remember { mutableIntStateOf(0) }
//    val tabTitles = listOf("Tab 1", "Tab 2", "Tab 3")
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Center,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        TabRow(selectedTabIndex = tabIndex) {
//            tabTitles.forEachIndexed { index, title ->
//                Tab(
//                    selected = tabIndex == index,
//                    onClick = { tabIndex = index },
//                    text = { Text(title) }
//                )
//            }
//        }
//        when(tabIndex) {
//            0 -> { Text(text = "Content 1", modifier = Modifier.padding(16.dp))}
//            1 -> { Text(text = "Content 2", modifier = Modifier.padding(16.dp))}
//            2 -> { Text(text = "Content 3", modifier = Modifier.padding(16.dp))}
//        }
//    }

//    Scaffold(
//        bottomBar = {
//            BottomAppBar(
//                content = {
//                    TabRow(selectedTabIndex = 0) {
//                        Tab(
//                            selected = true,
//                            onClick = { },
//                            text = { Text("Tab 1") }
//                        )
//                        Tab(
//                            selected = false,
//                            onClick = { },
//                            text = { Text("Tab 2") }
//                        )
//                        Tab(
//                            selected = false,
//                            onClick = { },
//                            text = { Text("Tab 3") }
//                        )
//                    }
//                }
//            )
//        }
//    ) {
//
//    }

}