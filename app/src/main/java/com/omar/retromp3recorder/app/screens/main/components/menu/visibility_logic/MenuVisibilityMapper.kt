package com.omar.retromp3recorder.app.screens.main.components.menu.visibility_logic

import com.omar.retromp3recorder.app.screens.main.components.menu.MenuContract
import kotlinx.coroutines.flow.Flow

interface MenuVisibilityMapperFlow {
    fun flow(): Flow<List<MenuContract.Item>>
}
