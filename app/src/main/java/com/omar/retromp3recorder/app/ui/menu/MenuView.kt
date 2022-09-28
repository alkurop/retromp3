package com.omar.retromp3recorder.app.ui.menu

import com.github.alkurop.ghostinshell.Shell
import com.omar.retromp3recorder.dto.MenuAction
import com.omar.retromp3recorder.dto.MenuExecutable

interface MenuView {
    data class State(
        val items: List<MenuAction> = emptyList(),
        val isVisible: Boolean = true,
        val popup: Shell<MenuExecutable> = Shell.empty()
    )
}
