package org.bonygod.gymroutine.ui.view.home.composables

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.profile_screen_change_password
import gymroutine.composeapp.generated.resources.profile_screen_subtitle_email
import gymroutine.composeapp.generated.resources.profile_screen_subtitle_fisic_state
import gymroutine.composeapp.generated.resources.profile_screen_subtitle_user
import gymroutine.composeapp.generated.resources.profile_screen_text_delete_account
import gymroutine.composeapp.generated.resources.profile_screen_text_logout
import gymroutine.composeapp.generated.resources.profile_screen_text_save
import gymroutine.composeapp.generated.resources.profile_screen_text_years
import gymroutine.composeapp.generated.resources.profile_screen_title_age
import gymroutine.composeapp.generated.resources.profile_screen_title_user_data
import gymroutine.composeapp.generated.resources.profile_screen_title_weight
import org.bonygod.gymroutine.ui.theme.CustomBlack
import org.bonygod.gymroutine.ui.theme.CustomGray
import org.bonygod.gymroutine.ui.theme.CustomLightGray
import org.bonygod.gymroutine.ui.theme.CustomWhite
import org.bonygod.gymroutine.ui.theme.CustomYellow
import org.jetbrains.compose.resources.stringResource

@Composable
fun TitleSubtitle() {
    Text(
        text = stringResource(Res.string.profile_screen_title_user_data),
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
            text = stringResource(Res.string.profile_screen_subtitle_user),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun UserNameTextField(
    userName: String,
    userNameDB: String,
    onValueChange: (userValue: String) -> Unit
) {
    TextField(
        value = userName,
        placeholder = {
            Text(
                text = userNameDB,
                fontSize = 16.sp
            )
        },
        onValueChange = { userValue ->
            onValueChange(userValue)
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
}

@Composable
fun EmailSection(userEmail: String) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, top = 30.dp, bottom = 10.dp)
    ) {
        Text(
            text = stringResource(Res.string.profile_screen_subtitle_email),
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
            text = userEmail,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
    Text(
        text = stringResource(Res.string.profile_screen_change_password),
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
}

@Composable
fun FisicStateSection(
    weight: String,
    weightDB: String,
    age: String,
    ageDB: String,
    onWeightValueChange: (weightValue: String) -> Unit,
    onAgeValueChange: (ageValue: String) -> Unit
) {

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(Res.string.profile_screen_subtitle_fisic_state),
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
                    text = stringResource(Res.string.profile_screen_title_weight),
                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                TextField(
                    value = weight,
                    placeholder = {
                        Text(
                            text = weightDB,
                            fontSize = 16.sp
                        )
                    },
                    suffix = {
                        Text(
                            text = "kg",
                            fontSize = 16.sp
                        )
                    },
                    onValueChange = { weightValue ->
                        onWeightValueChange(weightValue)
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
                    text = stringResource(Res.string.profile_screen_title_age),
                    modifier = Modifier.padding(top = 10.dp, bottom = 10.dp),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
                TextField(
                    value = age,
                    placeholder = {
                        Text(
                            text = ageDB,
                            fontSize = 16.sp
                        )
                    },
                    suffix = {
                        Text(
                            text = stringResource(Res.string.profile_screen_text_years),
                            fontSize = 16.sp
                        )
                    },
                    onValueChange = { ageValue ->
                        onAgeValueChange(ageValue)
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
}

@Composable
fun SaveButton(isButtonEnabled: Boolean, onButtonSaveClick: () -> Unit) {
    Button(
        onClick = {
            onButtonSaveClick()
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
            text = stringResource(Res.string.profile_screen_text_save),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun LogOutButton(onButtonLogOutClick: () -> Unit) {
    Button(
        onClick = {
            onButtonLogOutClick()
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
            text = stringResource(Res.string.profile_screen_text_logout),
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp
        )
    }
}

@Composable
fun DeleteAccountClickableText() {
    Text(
        text = stringResource(Res.string.profile_screen_text_delete_account),
        modifier = Modifier
            .padding(vertical = 10.dp)
            .clickable {

            },
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
        color = Color.Red
    )
}