package com.omar.retromp3recorder.app.screens.home.components.menu.visibility_logic.file

import com.omar.retromp3recorder.app.screens.home.components.menu.MenuContract
import com.omar.retromp3recorder.app.screens.home.components.menu.visibility_logic.MenuVisibilityMapperFlow
import com.omar.retromp3recorder.bl.audio.progress.AudioState
import com.omar.retromp3recorder.bl.audio.progress.AudioStateMapper
import com.omar.retromp3recorder.domain.MenuPopup
import com.omar.retromp3recorder.domain.PlayerRange
import com.omar.retromp3recorder.storage.repo.local.PlayerProgressRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

class CropFileMenuStateMapperFlow @Inject constructor(
    private val playerProgressRepo: PlayerProgressRepo,
    private val audioStateMapper: AudioStateMapper,
) : MenuVisibilityMapperFlow {
    override fun flow(): Flow<List<MenuContract.Item>> {
        return combine(
            playerProgressRepo.flow(),
            audioStateMapper.flow()
        ) { playerProgress, audioState ->
            val hasRange = playerProgress.value?.range?.notEmpty() ?: false
            val audioStateAllows = audioState is AudioState.Idle
            listOf(
                MenuContract.Item.Popup(
                    MenuPopup.Crop,
                    isEnabled = hasRange && audioStateAllows
                ),
            )
        }
    }
}

private fun PlayerRange.notEmpty(): Boolean = from > 0 || to < max
