package org.bonygod.gymroutine

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import org.bonygod.gymroutine.ui.theme.CustomBlack
import org.bonygod.gymroutine.ui.theme.CustomGray
import org.bonygod.gymroutine.ui.theme.CustomWhite
import org.bonygod.gymroutine.ui.theme.CustomYellow
import org.bonygod.gymroutine.ui.view.App
import org.bonygod.gymroutine.ui.view.components.CustomScrollSelector
import org.bonygod.gymroutine.ui.view.viewModels.SignUpViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            App()
        }

    }
}

@Preview(showSystemUi = true)
@Composable
fun AppAndroidPreview(signUpViewModel: SignUpViewModel = viewModel()) {

    val selectedWeight = remember { mutableIntStateOf(58) }
    val selectedHeight = remember { mutableIntStateOf(170) }
    val selectedAge = remember { mutableIntStateOf(25) }
    val selectedGender = remember { mutableStateOf("Hombre") }
    val inisitalValue = remember { mutableStateOf(true) }


    Column(
        modifier = Modifier.fillMaxSize().background(CustomBlack),
        horizontalAlignment = CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Peso", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = CustomYellow)
        Spacer(modifier = Modifier.padding(15.dp))
        CustomScrollSelector(selectedWeight.value, 30, 300) { newRound ->
            selectedWeight.value = newRound
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Text("Altura", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = CustomYellow)
        Spacer(modifier = Modifier.padding(15.dp))
        CustomScrollSelector(selectedHeight.value, 100, 250) { newRound ->
            selectedHeight.value = newRound
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Text("Edad", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = CustomYellow)
        Spacer(modifier = Modifier.padding(15.dp))
        CustomScrollSelector(selectedAge.value, 16, 100) { newRound ->
            selectedAge.value = newRound
        }
        Spacer(modifier = Modifier.padding(10.dp))
        Row {
            GenderButton(
                inisitalValue = inisitalValue.value,
                text = "Hombre",
                isSelected = selectedGender.value == "Hombre",
                onClick = {
                    selectedGender.value = "Hombre"
                    inisitalValue.value = false
                }
            )
            GenderButton(
                inisitalValue = inisitalValue.value,
                text = "Mujer",
                isSelected = selectedGender.value == "Mujer",
                onClick = {
                    selectedGender.value = "Mujer"
                    inisitalValue.value = false
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
                //navigateToUserProfile()
            },
            colors = ButtonDefaults.buttonColors(
                CustomYellow
            )
        ) {
            Text(
                text = "Finalizar",
                color = CustomBlack,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )

        }
    }
}

@Composable
fun GenderButton(inisitalValue: Boolean, text: String, isSelected: Boolean, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected && !inisitalValue) CustomYellow else CustomGray
        ),
        modifier = Modifier.padding(horizontal = 8.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected && !inisitalValue) CustomGray else CustomWhite,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
    }
}