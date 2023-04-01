package com.omar.retromp3recorder.app.screens.home.components.menu.visibility_logic.file

import com.omar.retromp3recorder.app.screens.home.components.menu.MenuContract
import com.omar.retromp3recorder.app.screens.home.components.menu.visibility_logic.MenuVisibilityMapperFlow
import com.omar.retromp3recorder.domain.MenuPopup
import com.omar.retromp3recorder.domain.PlayerRange
import com.omar.retromp3recorder.storage.repo.local.PlayerProgressRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CropFileMenuStateMapperFlow @Inject constructor(
    private val playerProgressRepo: PlayerProgressRepo,
) : MenuVisibilityMapperFlow {
    override fun flow(): Flow<List<MenuContract.Item>> {
        return playerProgressRepo.flow().map {
            val range = it.value?.range?.notEmpty() ?: false

            listOf(
                MenuContract.Item.Popup(
                    MenuPopup.Crop,
                    isEnabled = range
                ),
            )
        }
    }
}

private fun PlayerRange.notEmpty(): Boolean = from > 0 || to < max
