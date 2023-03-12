package com.omar.retromp3recorder.app.ui.menu

import com.omar.retromp3recorder.domain.MenuEnabler
import com.omar.retromp3recorder.domain.MenuPopup
import com.omar.retromp3recorder.utils.generic.Optional

interface MenuContract {
    data class State(
        val items: List<Item> = emptyList(),
        val isVisible: Boolean = true,
        val popup: Optional<MenuPopup> = Optional.empty()
    )

    sealed class Input {
        data class Enable(
            val enabler: MenuEnabler,
            val isEnabled: Boolean
        ) : Input()

        object Clear : Input()
        data class Popup(val action: MenuPopup) : Input()
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

fun MenuContract.Item.Popup.toInput() = MenuContract.Input.Popup(this.menuPopup)

