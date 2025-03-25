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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import org.bonygod.gymroutine.data.model.User
import org.bonygod.gymroutine.ui.theme.CustomBlack
import org.bonygod.gymroutine.ui.theme.CustomGray
import org.bonygod.gymroutine.ui.theme.CustomLightGray
import org.bonygod.gymroutine.ui.theme.CustomWhite
import org.bonygod.gymroutine.ui.theme.CustomYellow
import org.bonygod.gymroutine.ui.view.components.LoadingScreen
import org.bonygod.gymroutine.ui.view.viewModels.UserProfileViewModel
import org.bonygod.gymroutine.ui.view.viewModels.UserViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun ProfileScreen(
    modifier: Modifier,
    userProfileViewModel: UserProfileViewModel = koinViewModel(),
    navigateToLoginOrSignup: () -> Unit
) {
    var isButtonEnabled by remember { mutableStateOf(false) }
    val userData by userProfileViewModel.userData.collectAsState()
    val userDao: UserViewModel = koinViewModel()
    var user by remember { mutableStateOf<User?>(null) }
    var userName by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    LaunchedEffect(Unit){
        user = userDao.getUser().first()
    }

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
                value = userName,
                placeholder = {
                        Text(
                            text = userData?.userName ?: "",
                            fontSize = 16.sp
                        )
                },
                onValueChange = {
                    isButtonEnabled = it != userData?.userName
                    userName = it
                    if(userName != "") {
                        userProfileViewModel.userData.value?.userName = userName
                    }
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
                    .clip(RoundedCornerShape(15.dp)),
                colors = TextFieldDefaults.colors(
                    unfocusedContainerColor = CustomGray,
                    unfocusedPlaceholderColor = CustomYellow,
                    focusedContainerColor = CustomGray,
                    focusedPlaceholderColor = CustomYellow,
                    focusedTextColor = CustomYellow,
                    unfocusedTextColor = CustomYellow
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
                    text = userData?.email ?: "",
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
                            value = weight,
                            placeholder = {
                                Text(
                                    text = userData?.weight.toString(),
                                    fontSize = 16.sp
                                )
                            },
                            suffix = {
                                Text(
                                    text = "kg",
                                    fontSize = 16.sp
                                )
                            },
                            onValueChange = {
                                isButtonEnabled = it != userData?.weight.toString()
                                weight = it
                                if(weight != "") {
                                    userProfileViewModel.selectedHeight.value = weight.toInt()
                                }
                            },
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
                                focusedSuffixColor = CustomYellow,
                                focusedTextColor = CustomYellow,
                                unfocusedTextColor = CustomYellow
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
                            value = age,
                            placeholder = {
                                Text(
                                    text = userData?.age.toString(),
                                    fontSize = 16.sp
                                )
                            },
                            suffix = {
                                Text(
                                    text = "years",
                                    fontSize = 16.sp
                                )
                            },
                            onValueChange = {
                                isButtonEnabled = it != userData?.age.toString()
                                age = it
                                if(age != "") {
                                    userProfileViewModel.selectedAge.value = age.toInt()
                                }
                            },
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
                                focusedSuffixColor = CustomYellow,
                                focusedTextColor = CustomYellow,
                                unfocusedTextColor = CustomYellow
                            )
                        )
                    }
                }
            }
            Button(
                onClick = {
                        try {
                            if(user != null){
                                val upDateUser = User(
                                    user!!.id,
                                    user!!.email,
                                    userProfileViewModel.userData.value?.userName ?: "",
                                    user!!.token
                                )
                                userDao.updateUser(upDateUser)
                            }
                        } catch (e: Exception){
                            e.printStackTrace()
                        }
                    userProfileViewModel.userData.value?.userName = if (userName == "") userData?.userName ?: "" else userName
                    userProfileViewModel.selectedWeight.value = if (weight == "") userData?.weight ?: 0 else weight.toInt()
                    userProfileViewModel.selectedAge.value = if (age == "") userData?.age ?: 0 else age.toInt()
                    userProfileViewModel.selectedHeight.value = userData?.height ?: 0
                    userProfileViewModel.saveUserData()
                },
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
                onClick = {
                    userProfileViewModel.logOut(navigateToLoginOrSignup)
                },
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
