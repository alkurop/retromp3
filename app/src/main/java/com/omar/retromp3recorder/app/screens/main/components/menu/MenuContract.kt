package com.omar.retromp3recorder.app.screens.main.components.menu

import androidx.compose.runtime.Immutable
import com.omar.retromp3recorder.domain.MenuEnabler
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

    sealed class Item {
        data class Popup(
            val menuPopup: MenuPopup,
            val isEnabled: Boolean
        ) : Item()

        data class Enable(
            val enabler: MenuEnabler,
            val isOpen: Boolean,
            val isActive: Boolean
        ) : Item()
    }
}

fun MenuContract.Item.Enable.toInput() =
    MenuContract.Input.Enable(this.enabler, this.isOpen.not())
