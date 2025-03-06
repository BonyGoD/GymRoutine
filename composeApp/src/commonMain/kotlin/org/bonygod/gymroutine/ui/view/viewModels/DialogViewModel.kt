package org.bonygod.gymroutine.ui.view.viewModels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import gymroutine.composeapp.generated.resources.Res
import gymroutine.composeapp.generated.resources.ok_icon
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.jetbrains.compose.resources.DrawableResource

class DialogViewModel: ViewModel() {

    private val _showDialog = MutableStateFlow(false)
    val showDialog = _showDialog.asStateFlow()

    private val _customDialogTitle = MutableStateFlow("")
    val customDialogTitle = _customDialogTitle.asStateFlow()

    private val _customDialogSubtitle = MutableStateFlow("")
    val customDialogSubtitle = _customDialogSubtitle.asStateFlow()

    private val _customDialogErrorTitle = MutableStateFlow("")
    val customDialogErrorTitle = _customDialogErrorTitle.asStateFlow()

    private val _customDialogErrorSubtitle = MutableStateFlow("")
    val customDialogErrorSubtitle = _customDialogErrorSubtitle.asStateFlow()

    private val _icon = MutableStateFlow(Res.drawable.ok_icon)
    val icon = _icon.asStateFlow()

    private val _iconColor = MutableStateFlow(Color.Transparent)
    val iconColor = _iconColor.asStateFlow()

    fun onShowDialogChange(showDialog: Boolean) {
        _showDialog.value = showDialog
    }

    fun setCustomDialog(
        customDialogTitle: String,
        customDialogSubtitle: String,
        icon: DrawableResource,
        iconColor: Color
    ) {
        _customDialogTitle.value = customDialogTitle
        _customDialogSubtitle.value = customDialogSubtitle
        _icon.value = icon
        _iconColor.value = iconColor
    }

    fun setErrorMessageDialog(errorMessage: String) {
        _customDialogErrorTitle.value = errorMessage
    }
}