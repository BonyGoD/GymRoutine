package org.bonygod.gymroutine.ui.view.home.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import kotlinx.coroutines.flow.first
import org.bonygod.gymroutine.data.model.User
import org.bonygod.gymroutine.ui.theme.CustomLightGray
import org.bonygod.gymroutine.ui.view.home.composables.DeleteAccountClickableText
import org.bonygod.gymroutine.ui.view.home.composables.EmailSection
import org.bonygod.gymroutine.ui.view.home.composables.FisicStateSection
import org.bonygod.gymroutine.ui.view.home.composables.LogOutButton
import org.bonygod.gymroutine.ui.view.home.composables.SaveButton
import org.bonygod.gymroutine.ui.view.home.composables.TitleSubtitle
import org.bonygod.gymroutine.ui.view.home.composables.UserNameTextField
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
    var userName by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(CustomLightGray),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {

            TitleSubtitle()

            UserNameTextField(userName, userData?.userName ?: "") {
                isButtonEnabled = it != userData?.userName
                userName = it
                if (userName != "") {
                    userProfileViewModel.userData.value?.userName = userName
                }
            }

            EmailSection(userData?.email ?: "")

            FisicStateSection(
                weight, userData?.weight.toString(), age, userData?.age.toString(),
                onWeightValueChange = {
                    isButtonEnabled = it != userData?.weight.toString()
                    weight = it
                    if (weight != "") {
                        userProfileViewModel.selectedHeight.value = weight.toInt()
                    }
                },
                onAgeValueChange = {
                    isButtonEnabled = it != userData?.age.toString()
                    age = it
                    if (age != "") {
                        userProfileViewModel.selectedAge.value = age.toInt()
                    }
                })
            SaveButton(isButtonEnabled) {
                userProfileViewModel.updateUserDaoData()
                userProfileViewModel.userData.value?.userName = if (userName == "") userData?.userName ?: "" else userName
                userProfileViewModel.selectedWeight(if (weight == "") userData?.weight ?: 0 else weight.toInt())
                userProfileViewModel.selectedAge(if (age == "") userData?.age ?: 0 else age.toInt())
                userProfileViewModel.selectedHeight(userData?.height ?: 0)
                userProfileViewModel.saveUserData()
            }

            LogOutButton {
                userProfileViewModel.logOut()
                navigateToLoginOrSignup()
            }

            DeleteAccountClickableText()
        }
    }
}
