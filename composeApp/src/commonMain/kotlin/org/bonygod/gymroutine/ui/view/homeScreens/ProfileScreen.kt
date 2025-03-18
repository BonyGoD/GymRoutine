package org.bonygod.gymroutine.ui.view.homeScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.bonygod.gymroutine.ui.theme.CustomBlack
import org.bonygod.gymroutine.ui.theme.CustomGray
import org.bonygod.gymroutine.ui.theme.CustomLightGray
import org.bonygod.gymroutine.ui.theme.CustomWhite
import org.bonygod.gymroutine.ui.theme.CustomYellow
import org.bonygod.gymroutine.ui.view.viewModels.UserProfileViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier,
    userProfileViewModel: UserProfileViewModel = koinViewModel(),
    navigateToLoginOrSignup: () -> Unit
) {
    var isButtonEnabled by remember { mutableStateOf(true) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CustomLightGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            Text(
                text = "Datos",
                modifier = Modifier
                    .padding(start = 20.dp, top = 30.dp, bottom = 10.dp),
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 30.dp, bottom = 10.dp)
            ) {
                Text(
                    text = "Usuario",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
            }
            TextField(
                value = "",
                placeholder = {
                    Text(
                        text = "Ivan Boniquet Rodriguez",
                        fontSize = 16.sp
                    )
                },
                onValueChange = {},
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(15.dp)),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = CustomGray,
                    unfocusedPlaceholderColor = CustomYellow,
                    focusedContainerColor = CustomGray,
                    focusedPlaceholderColor = CustomYellow
                )
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 30.dp, bottom = 10.dp)
            ) {
                Text(
                    text = "Email",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start
                )
            }
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 15.dp, bottom = 10.dp)
            ) {
                Text(
                    text = "i.boniquet@gmail.com",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = "Cambiar de contraseña",
                modifier = Modifier
                    .padding(start = 20.dp, top = 20.dp, bottom = 10.dp)
                    .clickable { },
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
                    .border(1.dp, CustomYellow)
            )
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Estado físico",
                    modifier = Modifier
                        .padding(start = 20.dp, top = 10.dp)
                        .align(Alignment.CenterHorizontally),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
                Row(modifier = Modifier.padding(top = 20.dp, bottom = 10.dp)) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Peso",
                            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        TextField(
                            value = "",
                            placeholder = {
                                Text(
                                    text = "75",
                                    fontSize = 16.sp
                                )
                            },
                            suffix = {
                                Text(
                                    text = " kg",
                                    fontSize = 16.sp
                                )
                            },
                            onValueChange = {},
                            singleLine = true,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .width(100.dp),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = CustomGray,
                                unfocusedPlaceholderColor = CustomYellow,
                                focusedContainerColor = CustomGray,
                                focusedPlaceholderColor = CustomYellow,
                                unfocusedSuffixColor = CustomYellow,
                                focusedSuffixColor = CustomYellow
                            )
                        )
                    }
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Edad",
                            modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        TextField(
                            value = "",
                            placeholder = {
                                Text(
                                    text = "39",
                                    fontSize = 16.sp
                                )
                            },
                            suffix = {
                                Text(
                                    text = " years",
                                    fontSize = 16.sp
                                )
                            },
                            onValueChange = {},
                            singleLine = true,
                            modifier = Modifier
                                .padding(horizontal = 20.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .width(100.dp),
                            colors = TextFieldDefaults.colors(
                                unfocusedContainerColor = CustomGray,
                                unfocusedPlaceholderColor = CustomYellow,
                                focusedContainerColor = CustomGray,
                                focusedPlaceholderColor = CustomYellow,
                                unfocusedSuffixColor = CustomYellow,
                                focusedSuffixColor = CustomYellow
                            )
                        )
                    }
                }
            }
            Button(
                onClick = { },
                enabled = isButtonEnabled,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 20.dp)
                    .border(1.dp, CustomBlack, RoundedCornerShape(30.dp))
                    .clip(shape = RoundedCornerShape(30.dp))
                    .height(50.dp),
                colors = ButtonColors(
                    containerColor = CustomYellow,
                    contentColor = CustomBlack,
                    disabledContainerColor = CustomLightGray,
                    disabledContentColor = CustomBlack
                )
            ) {
                Text(
                    text = "Guardar",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            Button(
                onClick = { },
                modifier = Modifier
                    .padding(horizontal = 20.dp, vertical = 20.dp)
                    .border(1.dp, CustomBlack, RoundedCornerShape(30.dp))
                    .clip(shape = RoundedCornerShape(30.dp))
                    .height(50.dp),
                colors = ButtonColors(
                    containerColor = Color.Red,
                    contentColor = CustomWhite,
                    disabledContainerColor = Color.Red,
                    disabledContentColor = CustomWhite
                )
            ) {
                Text(
                    text = "LogOut",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            Text(
                text = "Eliminar mi cuenta",
                modifier = Modifier
                    .padding(vertical = 10.dp)
                    .clickable { },
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Color.Red
            )
        }
    }
}