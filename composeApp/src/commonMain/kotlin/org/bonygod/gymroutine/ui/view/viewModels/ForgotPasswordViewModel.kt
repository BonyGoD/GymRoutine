package org.bonygod.gymroutine.ui.view.viewModels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.exclamation
import gymroutine.composeapp.generated.resources.ok_icon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.bonygod.gymroutine.domain.ForgotPasswordUseCase
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ForgotPasswordViewModel: ViewModel(), KoinComponent {

    private val forgotPasswordUseCase: ForgotPasswordUseCase by inject()

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _customDialogTitle = MutableStateFlow("")
    val customDialogTitle = _customDialogTitle.asStateFlow()

    private val _customDialogSubtitle = MutableStateFlow("")
    val customDialogSubtitle = _customDialogSubtitle.asStateFlow()

    private val _icon = MutableStateFlow(Res.drawable.ok_icon)
    val icon = _icon.asStateFlow()

    private val _buttonVisible = MutableStateFlow(false)
    val buttonVisible = _buttonVisible.asStateFlow()

    fun validEmail(validEmail: Boolean) {
        _buttonVisible.value = validEmail
    }

    fun setTitlesOkDialog(title: String, subtitle: String) {
        _customDialogTitle.value = title
        _customDialogSubtitle.value = subtitle
    }

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun resetEmail(
        dialogViewModel: DialogViewModel,
        sharedViewModel: SharedViewModel,
        onBack: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                forgotPasswordUseCase(_email.value)
                dialogViewModel.setCustomDialog(
                    customDialogTitle.value,
                    customDialogSubtitle.value,
                    Res.drawable.ok_icon,
                    Color.Green
                )
                sharedViewModel.onShowDialogChange(true)
                onBack()
            } catch (e: Exception) {
                dialogViewModel.setCustomDialog(
                    dialogViewModel.customDialogErrorTitle.value,
                    "",
                    Res.drawable.exclamation,
                    Color.Red
                )
                dialogViewModel.onShowDialogChange(true)
            }
        }
    }
}