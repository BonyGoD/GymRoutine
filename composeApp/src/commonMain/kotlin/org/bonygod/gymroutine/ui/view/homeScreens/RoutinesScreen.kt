package org.bonygod.gymroutine.ui.view.homeScreens

import androidx.compose.foundation.background
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
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.bonygod.gymroutine.ui.theme.CustomBlack
import org.bonygod.gymroutine.ui.theme.CustomGray
import org.bonygod.gymroutine.ui.theme.CustomLightGray
import org.bonygod.gymroutine.ui.theme.CustomWhite
import org.bonygod.gymroutine.ui.theme.CustomYellow

@Composable
fun RoutinesScreen(modifier: Modifier) {
    val maxLength = 21
    val text = "Extension de triceps con polea"
    val truncatedText = text.take(maxLength) + "..."
    LazyColumn(
        modifier = modifier.fillMaxSize().background(CustomLightGray),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.Start
    ) {
        item {
            Text(
                text = "Fuerza", modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 5.dp)
                    .clip(CircleShape)
                    .background(CustomGray)
                    .padding(horizontal = 20.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(text = truncatedText, modifier = Modifier.weight(1f), maxLines = 1, color = CustomWhite)
                Text(text = "4 S", modifier = Modifier.width(40.dp), color = CustomWhite)
                Text(text = "10 R", modifier = Modifier.width(40.dp), color = CustomWhite)
                Text(text = "60 Seg", modifier = Modifier.width(60.dp), color = CustomWhite)
            }
            Spacer(modifier = Modifier.padding(5.dp))
        }
    }
    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.BottomEnd) {
        FloatingActionButton(
            onClick = { /*TODO*/ },
            modifier = Modifier.padding(16.dp),
            containerColor = CustomYellow,
            contentColor = CustomBlack,
            elevation = FloatingActionButtonDefaults.elevation(8.dp)
        ) {
            Icon(Icons.Rounded.Add, contentDescription = "Add")
        }
    }
}