package org.bonygod.gymroutine.ui.view.viewModels

import androidx.lifecycle.ViewModel

class DialogViewModel: ViewModel() {

    var titleDialog: String? = null
    var subtitleDialog: String? = null

    fun setDialogTitles(titleDialog: String?, subtitleDialog: String?) {
        this.titleDialog = titleDialog
        this.subtitleDialog = subtitleDialog
    }
}