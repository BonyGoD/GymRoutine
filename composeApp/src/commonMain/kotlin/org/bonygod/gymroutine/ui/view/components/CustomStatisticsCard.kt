package org.bonygod.gymroutine.ui.view.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.Urbanist
import gymroutine.composeapp.generated.resources.activity
import gymroutine.composeapp.generated.resources.flame
import org.bonygod.gymroutine.ui.theme.CustomBlack
import org.bonygod.gymroutine.ui.theme.CustomGray
import org.bonygod.gymroutine.ui.theme.CustomLightGray
import org.bonygod.gymroutine.ui.theme.CustomWhite
import org.bonygod.gymroutine.ui.theme.CustomYellow
import org.jetbrains.compose.resources.Font
import org.jetbrains.compose.resources.painterResource

@Composable
fun CustomStatisticsCard() {
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
                modifier = Modifier
                    .height(210.dp)
                    .align(CenterHorizontally)
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
                modifier = Modifier
                    .height(210.dp)
                    .align(CenterHorizontally)
            ) {
                Spacer(modifier = Modifier.weight(1f))
                Box(
                    modifier = Modifier.align(CenterHorizontally)
                ) {
                    Icon(
                        painter = painterResource(Res.drawable.activity),
                        modifier = Modifier.size(100.dp),
                        contentDescription = "",
                        tint = CustomYellow
                    )
                }
                Spacer(modifier = Modifier.weight(1f))
                Text(
                    modifier = Modifier.padding(bottom = 5.dp),
                    text = "120 h",
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
}