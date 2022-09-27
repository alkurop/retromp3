package com.omar.retromp3recorder.app.ui.menu

import com.omar.retromp3recorder.dto.MenuAction

interface MenuView {
    data class State(
        val items: List<MenuAction> = emptyList(),
        val isVisible: Boolean = true
    )
}
