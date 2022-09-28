package com.omar.retromp3recorder.app.ui.menu.popups

import androidx.compose.runtime.Composable
import com.omar.retromp3recorder.dto.MenuExecutable

object MenuPopups {
    @Composable
    fun showPopup(menuExecutable: MenuExecutable) {
        when (menuExecutable) {
            MenuExecutable.Crop -> CropPopup()
        }
    }
}
