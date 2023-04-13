package com.omar.retromp3recorder.app.screens.home.components.menu.visibility_logic

import com.omar.retromp3recorder.app.screens.home.components.menu.MenuContract
import com.omar.retromp3recorder.app.screens.home.components.menu.visibility_logic.merged.FileActionsStateMapperFlow
import com.omar.retromp3recorder.bl.audio.progress.AudioState
import com.omar.retromp3recorder.bl.audio.progress.AudioStateMapper
import com.omar.retromp3recorder.domain.*
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
                    ) { it.toList().flatten().sortedBy { item -> item.menu.position } }
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
    return this.map { (_, range, _, speed, speech) ->
        listOfNotNull(
            MenuContract.Item.Enable(
                VisibilityEnabler.RangeBar,
                isOpen = range.isVisible,
                isActive = range.isActive
            ),

            MenuContract.Item.Popup(
                MenuPopup.Search,
                file.value?.path != null
            ),

            MenuContract.Item.Enable(
                VisibilityEnabler.PlaybackSpeed,
                isOpen = speed.isVisible,
                isActive = speed.isEnabled
            ),

            MenuContract.Item.Enable(
                VisibilityEnabler.SpeechRecognition,
                isOpen = speech.isVisible,
                isActive = speech.isEnabled
            ),
        )
    }
}
