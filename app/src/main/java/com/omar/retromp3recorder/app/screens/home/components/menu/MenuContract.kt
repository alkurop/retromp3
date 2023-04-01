package com.omar.retromp3recorder.app.screens.home.components.menu

import androidx.compose.runtime.Immutable
import com.omar.retromp3recorder.domain.MenuEnabler
import com.omar.retromp3recorder.domain.MenuItem
import com.omar.retromp3recorder.domain.MenuPopup

interface MenuContract {
    @Immutable
    data class State(
        val items: List<Item> = emptyList(),
        val isVisible: Boolean = true,
    )

    sealed class Input {
        data class Enable(
            val enabler: MenuEnabler,
            val isEnabled: Boolean
        ) : Input()

    }

    sealed class Item(val menu: MenuItem) {
        data class Popup(
            val menuPopup: MenuPopup,
            val isEnabled: Boolean
        ) : Item(menuPopup)

        data class Enable(
            val enabler: MenuEnabler,
            val isOpen: Boolean,
            val isActive: Boolean
        ) : Item(enabler)
    }
}

fun MenuContract.Item.Enable.toInput() =
    MenuContract.Input.Enable(this.enabler, this.isOpen.not())
