package com.omar.retromp3recorder.app.nav

sealed interface AppDestination {
    val route: String
    val type: DestinationType


    // region screen
    object SearchScreen : AppDestination {
        override val route: String = "search_screen"
        override val type: DestinationType = DestinationType.Screen
    }

    object HomeScreen : AppDestination {
        override val route: String = "home_screen"
        override val type: DestinationType = DestinationType.Screen
    }

    object SettingScreen : AppDestination {
        override val route: String = "setting_screen"
        override val type: DestinationType = DestinationType.Screen
    }
//endregion

    //region popup
    object CropPopup : AppDestination {
        override val route: String = "crop_popup"
        override val type: DestinationType = DestinationType.Popup
    }

    object RenamePopup : AppDestination {
        override val route: String = "rename_popup"
        override val type: DestinationType = DestinationType.Popup
    }

    object DeletePopup : AppDestination {
        override val route: String = "delete_popup"
        override val type: DestinationType = DestinationType.Popup
    }
//endregion


}

enum class DestinationType {
    Popup,
    Screen
}
