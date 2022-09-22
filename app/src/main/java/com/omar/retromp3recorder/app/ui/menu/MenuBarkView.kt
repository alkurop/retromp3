package com.omar.retromp3recorder.app.ui.menu

import com.omar.retromp3recorder.dto.MenuAction

interface MenuBarkView {
    data class State(
        val actions: List<MenuAction>,
        val isVisible: Boolean
    )
}


