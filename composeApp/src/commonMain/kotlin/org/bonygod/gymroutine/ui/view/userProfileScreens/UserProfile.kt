package org.bonygod.gymroutine.ui.view.userProfileScreens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.user_profile_age
import gymroutine.composeapp.generated.resources.user_profile_button
import gymroutine.composeapp.generated.resources.user_profile_height
import gymroutine.composeapp.generated.resources.user_profile_man
import gymroutine.composeapp.generated.resources.user_profile_weight
import gymroutine.composeapp.generated.resources.user_profile_women
import org.bonygod.gymroutine.ui.theme.CustomBlack
import org.bonygod.gymroutine.ui.theme.CustomYellow
import org.bonygod.gymroutine.ui.view.components.CustomScrollSelector
import org.bonygod.gymroutine.ui.view.components.CustomSelectableButton
import org.bonygod.gymroutine.ui.view.viewModels.UserProfileViewModel
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel

private const val MAN = "Hombre"
private const val WOMEN = "Mujer"

@Composable
fun UserProfile(userProfileViewModel: UserProfileViewModel = koinViewModel(), navigateToDashboard: () -> Unit) {

    val selectedWeight = userProfileViewModel.selectedWeight.collectAsState()
    val selectedHeight = userProfileViewModel.selectedHeight.collectAsState()
    val selectedAge = userProfileViewModel.selectedAge.collectAsState()
    val selectedGender = userProfileViewModel.selectedGender.collectAsState()
    val inisitalValue = userProfileViewModel.inisitalValue.collectAsState()

    Column(
        modifier = Modifier.fillMaxSize().background(CustomBlack),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(stringResource(Res.string.user_profile_weight), fontSize = 30.sp, fontWeight = FontWeight.Bold, color = CustomYellow)
        Spacer(modifier = Modifier.padding(15.dp))
        CustomScrollSelector(selectedWeight.value, 30, 300) { newWeight ->
            userProfileViewModel.selectedWeight(newWeight)
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Text(stringResource(Res.string.user_profile_height), fontSize = 30.sp, fontWeight = FontWeight.Bold, color = CustomYellow)
        Spacer(modifier = Modifier.padding(15.dp))
        CustomScrollSelector(selectedHeight.value, 100, 250) { newHeight ->
            userProfileViewModel.selectedHeight(newHeight)
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Text(stringResource(Res.string.user_profile_age), fontSize = 30.sp, fontWeight = FontWeight.Bold, color = CustomYellow)
        Spacer(modifier = Modifier.padding(15.dp))
        CustomScrollSelector(selectedAge.value, 16, 100) { newAge ->
            userProfileViewModel.selectedAge(newAge)
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Row {
            CustomSelectableButton(
                inisitalValue = inisitalValue.value,
                text = stringResource(Res.string.user_profile_man),
                isSelected = selectedGender.value == MAN,
                onClick = {
                    userProfileViewModel.selectedGender(MAN)
                    userProfileViewModel.inisitalValue(false)
                }
            )
            CustomSelectableButton(
                inisitalValue = inisitalValue.value,
                text = stringResource(Res.string.user_profile_women),
                isSelected = selectedGender.value == WOMEN,
                onClick = {
                    userProfileViewModel.selectedGender(WOMEN)
                    userProfileViewModel.inisitalValue(false)
                }
            )
        }
        Spacer(modifier = Modifier.padding(25.dp))
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)
                .height(50.dp),
            onClick = {
                navigateToDashboard()
            },
            colors = ButtonDefaults.buttonColors(
                CustomYellow
            )
        ) {
            Text(
                text = stringResource(Res.string.user_profile_button),
                color = CustomBlack,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

        }
    }
}








