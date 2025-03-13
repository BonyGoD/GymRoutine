package org.bonygod.gymroutine.ui.view.homeScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.dashboard_statistics_title
import gymroutine.composeapp.generated.resources.dashboard_today_sesion_title
import kotlinx.datetime.Clock
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import org.bonygod.gymroutine.ui.theme.CustomBlack
import org.bonygod.gymroutine.ui.theme.CustomGray
import org.bonygod.gymroutine.ui.theme.CustomLightGray
import org.bonygod.gymroutine.ui.view.components.CustomHorizontalCalendar
import org.bonygod.gymroutine.ui.view.components.CustomStatisticsCard
import org.bonygod.gymroutine.ui.view.components.CustomTodaySessionCard
import org.jetbrains.compose.resources.stringResource

@Composable
fun DashboardScreen(modifier: Modifier) {
    val today = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault()).date
    var selectedDate by remember { mutableStateOf(today) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(CustomLightGray)
    ) {
        CustomHorizontalCalendar(
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
                Text(
                    text = stringResource(Res.string.dashboard_today_sesion_title),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    textAlign = TextAlign.Start,
                    color = CustomBlack,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                CustomTodaySessionCard()
                Text(
                    text = stringResource(Res.string.dashboard_statistics_title),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 10.dp),
                    textAlign = TextAlign.Start,
                    color = CustomBlack,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.ExtraBold
                )
                CustomStatisticsCard()
            }
        }
    }
}


