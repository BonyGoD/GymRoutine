package org.bonygod.gymroutine

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.Urbanist
import gymroutine.composeapp.generated.resources.flame
import kotlinx.datetime.Clock
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toLocalDateTime
import org.bonygod.gymroutine.ui.theme.CustomBlack
import org.bonygod.gymroutine.ui.theme.CustomGray
import org.bonygod.gymroutine.ui.theme.CustomLightGray
import org.bonygod.gymroutine.ui.theme.CustomWhite
import org.bonygod.gymroutine.ui.theme.CustomYellow
import org.bonygod.gymroutine.ui.view.App
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource

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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Preview(showSystemUi = true)
@Composable
fun AppAndroidPreview() {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    var selectedDate by remember { mutableStateOf(today) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(CustomLightGray)
    ) {
        HorizontalCalendar(
            selectedDate = selectedDate,
            onDateSelected = { date -> selectedDate = date }
        )
        HorizontalDivider(
            modifier = Modifier.padding(vertical = 20.dp, horizontal = 20.dp),
            thickness = 1.dp,
            color = CustomGray
        )
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = CenterHorizontally
        ) {
            item {
                Row {
                    Card(
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = CustomGray,
                            contentColor = CustomBlack,
                            disabledContainerColor = CustomLightGray,
                            disabledContentColor = CustomBlack
                        )
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.flame),
                            modifier = Modifier.size(150.dp),
                            contentDescription = ""
                        )
                    }
                }
                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 20.dp),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = CustomGray,
                            contentColor = CustomWhite,
                            disabledContainerColor = CustomLightGray,
                            disabledContentColor = CustomBlack
                        )
                    ) {
                        Column(
                            horizontalAlignment = CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.height(210.dp).align(CenterHorizontally)
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.flame),
                                modifier = Modifier.size(150.dp),
                                contentDescription = ""
                            )
                            Text(
                                text = "350 Kcal",
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                style = TextStyle(
                                    shadow = Shadow(
                                        color = CustomBlack,
                                        offset = Offset(6f, 6f),
                                        blurRadius = 3f
                                    ),
                                    fontFamily = FontFamily(Font(Res.font.Urbanist))
                                )
                            )
                        }
                    }
                    Spacer(modifier = Modifier.padding(5.dp))
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .padding(end = 20.dp),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 5.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = CustomGray,
                            contentColor = CustomWhite,
                            disabledContainerColor = CustomLightGray,
                            disabledContentColor = CustomBlack
                        )
                    ) {
                        Column(
                            horizontalAlignment = CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.height(210.dp).align(CenterHorizontally)
                        ) {
                            Image(
                                painter = painterResource(Res.drawable.flame),
                                modifier = Modifier.size(150.dp),
                                contentDescription = ""
                            )
                            Text(
                                text = "350 Kcal",
                                fontSize = 40.sp,
                                fontWeight = FontWeight.Bold,
                                style = TextStyle(
                                    shadow = Shadow(
                                        color = CustomBlack,
                                        offset = Offset(6f, 6f),
                                        blurRadius = 3f
                                    ),
                                    fontFamily = FontFamily(Font(Res.font.Urbanist))
                                )
                            )
                        }
                    }
                }
                Row {
                    Card(
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 20.dp, vertical = 10.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = CustomGray,
                            contentColor = CustomBlack,
                            disabledContainerColor = CustomLightGray,
                            disabledContentColor = CustomBlack
                        )
                    ) {
                        Image(
                            painter = painterResource(Res.drawable.flame),
                            modifier = Modifier.size(150.dp),
                            contentDescription = ""
                        )
                    }
                }
            }
        }
    }
}


object DayOfWeekHelper {
    private val translations = mapOf(
        "en" to listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"),
        "es" to listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom"),
        "ca" to listOf("Dil", "Dim", "Dim", "Dij", "Div", "Dis", "Diu")
    )

    fun getShortDayName(dayOfWeek: DayOfWeek, language: String = "en"): String {
        val lang = translations[language] ?: translations["en"]!!
        return lang[dayOfWeek.ordinal] // `ordinal` devuelve 0 para Lunes, 1 para Martes, etc.
    }
}

@Composable
fun HorizontalCalendar(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit
) {
    val dates = remember { generateDates() }
    val todayIndex =
        dates.indexOf(Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date)
    val listState = rememberLazyListState()
    val locale = Locale.current


    LaunchedEffect(todayIndex) {
        listState.scrollToItem(todayIndex)
    }

    LazyRow(
        state = listState,
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(dates) { date ->
            val dayName = DayOfWeekHelper.getShortDayName(date.dayOfWeek, locale.language)
            val dayNumber = date.dayOfMonth.toString()
            Surface(
                color = Color.Transparent,
                modifier = Modifier
                    .padding(horizontal = 4.dp)
                    .clickable { onDateSelected(date) }
            ) {
                Column(
                    horizontalAlignment = CenterHorizontally,
                    modifier = Modifier.padding(8.dp)
                ) {
                    Text(
                        text = dayName,
                        color = if (date == selectedDate) CustomYellow else CustomBlack,
                        fontSize = 16.sp
                    )
                    Text(
                        text = dayNumber,
                        color = if (date == selectedDate) CustomYellow else CustomBlack,
                        fontSize = 24.sp
                    )
                }
            }
        }
    }
}

fun generateDates(): List<LocalDate> {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date.minus(
        30,
        DateTimeUnit.DAY
    )
    return (0..61).map { today.plus(it, DateTimeUnit.DAY) }
}