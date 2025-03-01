package org.bonygod.gymroutine.ui.view.viewModels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dev.gitlive.firebase.auth.FirebaseAuth
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.exclamation
import gymroutine.composeapp.generated.resources.ok_icon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel: ViewModel() {

    private val _email = MutableStateFlow("")
    val email = _email.asStateFlow()

    private val _customDialogTitle = MutableStateFlow("")
    val customDialogTitle = _customDialogTitle.asStateFlow()

    private val _customDialogSubtitle = MutableStateFlow("")
    val customDialogSubtitle = _customDialogSubtitle.asStateFlow()

    private val _icon = MutableStateFlow(Res.drawable.ok_icon)
    val icon = _icon.asStateFlow()

    fun setTitlesOkDialog(title: String, subtitle: String) {
        _customDialogTitle.value = title
        _customDialogSubtitle.value = subtitle
    }

    fun onEmailChange(email: String) {
        _email.value = email
    }

    fun resetEmail(
        auth: FirebaseAuth,
        dialogViewModel: DialogViewModel,
        sharedViewModel: SharedViewModel,
        onBack: () -> Unit
    ) {
        viewModelScope.launch {
            try {
                //auth.sendPasswordResetEmail(email.value)
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
                    dialogViewModel.errorMessageDialog.value,
                    "",
                    Res.drawable.exclamation,
                    Color.Red
                )
                dialogViewModel.onShowDialogChange(true)
            }
        }
    }
}