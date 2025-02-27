package org.bonygod.gymroutine.ui.view.viewModels

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.ViewModel
import org.jetbrains.compose.resources.DrawableResource

class DialogViewModel: ViewModel() {

    var titleDialog: String? = null
    var subtitleDialog: String? = null
    var icon: DrawableResource? = null
    var iconColor: Color? = null

    fun setCustomDialog(titleDialog: String?, subtitleDialog: String?, icon: DrawableResource?, iconColor: Color?) {
        this.titleDialog = titleDialog
        this.subtitleDialog = subtitleDialog
        this.icon = icon
        this.iconColor = iconColor
    }
}