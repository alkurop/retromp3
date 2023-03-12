package com.omar.retromp3recorder.app.ui.menu

import com.omar.retromp3recorder.domain.MenuEnabler
import com.omar.retromp3recorder.domain.MenuPopup

interface MenuContract {
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
            val isEnabled: Boolean
        ) : Item()
    }
}

fun MenuContract.Item.Enable.toInput() =
    MenuContract.Input.Enable(this.enabler, this.isEnabled.not())
