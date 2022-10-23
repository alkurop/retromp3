package com.omar.retromp3recorder.app.ui.menu.container.logic

import com.omar.retromp3recorder.dto.MenuEnabler
import com.omar.retromp3recorder.dto.MenuExecutable
import com.omar.retromp3recorder.utils.Optional

interface MenuContract {
    data class State(
        val items: List<Item> = emptyList(),
        val isVisible: Boolean = true,
        val popup: Optional<MenuExecutable> = Optional.empty()
    )

    sealed class Input {
        data class Enable(
            val enabler: MenuEnabler,
            val isEnabled: Boolean
        ) : Input()

        object Clear : Input()
        data class Popup(val action: MenuExecutable) : Input()
    }

    sealed class Item {
        data class Popup(
            val menuExecutable: MenuExecutable,
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

fun MenuContract.Item.Popup.toInput() = MenuContract.Input.Popup(this.menuExecutable)

