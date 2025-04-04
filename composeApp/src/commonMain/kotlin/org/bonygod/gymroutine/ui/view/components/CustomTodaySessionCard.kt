package org.bonygod.gymroutine.ui.view.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.flame
import gymroutine.composeapp.generated.resources.timer
import org.bonygod.gymroutine.ui.theme.CustomBlack
import org.bonygod.gymroutine.ui.theme.CustomGray
import org.bonygod.gymroutine.ui.theme.CustomLightGray
import org.bonygod.gymroutine.ui.theme.CustomWhite
import org.bonygod.gymroutine.ui.theme.CustomYellow
import org.jetbrains.compose.resources.painterResource

@Composable
fun CustomTodaySessionCard(onCardClick: () -> Unit) {
    Row {
        Card(
            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 10.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp)
                .clickable {
                    onCardClick()
                },
            colors = CardDefaults.cardColors(
                containerColor = CustomGray,
                contentColor = CustomBlack,
                disabledContainerColor = CustomLightGray,
                disabledContentColor = CustomBlack
            )
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(CustomYellow)
                        .padding(4.dp)
                ) {
                    Text(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(CustomYellow),
                        text = "Day 11",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = CustomBlack
                    )
                }
                Spacer(modifier = Modifier.padding(8.dp))
                Text(
                    text = "Fuerza",
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    color = CustomWhite
                )
                Row(
                    modifier = Modifier.padding(top = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        painterResource(Res.drawable.timer),
                        contentDescription = "Timer",
                        tint = CustomYellow,
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 5.dp)
                    )
                    Text(
                        text = "30 min",
                        color = CustomWhite,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.padding(10.dp))
                    Icon(
                        painterResource(Res.drawable.flame),
                        contentDescription = "Flame",
                        tint = CustomYellow,
                        modifier = Modifier
                            .size(30.dp)
                            .padding(end = 5.dp)
                    )
                    Text(
                        text = "350 Kcal",
                        color = CustomWhite,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Icon(
                        Icons.Rounded.PlayArrow,
                        contentDescription = "Play",
                        tint = CustomBlack,
                        modifier = Modifier
                            .size(30.dp)
                            .clip(CircleShape)
                            .background(CustomYellow)
                    )
                }
            }
        }
    }
}