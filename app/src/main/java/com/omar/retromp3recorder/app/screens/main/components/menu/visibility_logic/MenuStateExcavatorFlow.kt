package com.omar.retromp3recorder.app.screens.main.components.menu.visibility_logic

import com.omar.retromp3recorder.app.screens.main.components.menu.MenuContract
import com.omar.retromp3recorder.app.screens.main.components.menu.visibility_logic.merged.FileActionsStateMapperFlow
import com.omar.retromp3recorder.bl.audio.progress.AudioState
import com.omar.retromp3recorder.bl.audio.progress.AudioStateMapper
import com.omar.retromp3recorder.domain.FileWrapper
import com.omar.retromp3recorder.domain.MenuPopup
import com.omar.retromp3recorder.domain.PlayerControls
import com.omar.retromp3recorder.domain.VisibilityEnabler
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import com.omar.retromp3recorder.utils.domain.Optional
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class MenuStateExcavatorFlow @Inject constructor(
    private val playerControlsRepo: PlayerControlsRepo,
    private val audioStateMapper: AudioStateMapper,
    private val currentFileRepo: CurrentFileRepo,
    private val fileActionsStateMapper: FileActionsStateMapperFlow
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun flow(): Flow<MenuContract.State> {
        return combine(
            currentFileRepo
                .flow()
                .flatMapLatest { file ->
                    combine(
                        listOf(
                            playerControlsRepo.flow().toMenuItems(file),
                            fileActionsStateMapper.flow()
                        )
                    ) { it.toList().flatten() }
                },
            audioStateMapper.flow()
        ) { menu, audioState ->
            MenuContract.State(
                items = menu,
                isVisible = audioState != AudioState.Recording,
            )
        }
    }
}

private fun Flow<PlayerControls>.toMenuItems(file: Optional<out FileWrapper>): Flow<List<MenuContract.Item>> {
    return this.map { (_, range, _, _) ->
        listOfNotNull(
            MenuContract.Item.Enable(
                VisibilityEnabler.RangeBar,
                isEnabled = range.isVisible
            ),
            MenuContract.Item.Popup(
                MenuPopup.Crop,
                isEnabled = range.isVisible
            ),
            MenuContract.Item.Popup(
                MenuPopup.Search,
                file.value?.path != null
            )
        )
    }
}
