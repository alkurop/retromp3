package com.omar.retromp3recorder.app.screens.home.components.menu.visibility_logic

import com.omar.retromp3recorder.app.screens.home.components.menu.MenuContract
import kotlinx.coroutines.flow.Flow

interface MenuVisibilityMapperFlow {
    fun flow(): Flow<List<MenuContract.Item>>
}
