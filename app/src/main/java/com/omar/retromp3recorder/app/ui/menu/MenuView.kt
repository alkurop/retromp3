package com.omar.retromp3recorder.app.ui.menu

import com.omar.retromp3recorder.dto.MenuAction

interface MenuView {
    data class State(
        val actions: List<MenuAction>,
        val isVisible: Boolean = true
    )
}


