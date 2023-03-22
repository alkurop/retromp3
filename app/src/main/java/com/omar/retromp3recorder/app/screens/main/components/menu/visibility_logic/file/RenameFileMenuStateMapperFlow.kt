package com.omar.retromp3recorder.app.screens.main.components.menu.visibility_logic.file

import com.omar.retromp3recorder.app.screens.main.components.menu.MenuContract
import com.omar.retromp3recorder.app.screens.main.components.menu.visibility_logic.MenuVisibilityMapperFlow
import com.omar.retromp3recorder.bl.audio.progress.AudioState
import com.omar.retromp3recorder.bl.audio.progress.AudioStateMapper
import com.omar.retromp3recorder.domain.MenuPopup
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RenameFileMenuStateMapperFlow @Inject constructor(
    private val audioStateMapper: AudioStateMapper,
    private val currentFileRepo: CurrentFileRepo
) : MenuVisibilityMapperFlow {
    override fun flow(): Flow<List<MenuContract.Item>> {
        return combine(
            currentFileRepo.flow(),
            audioStateMapper.flow()
        ) { currentFile, audioState ->
            when (audioState) {
                is AudioState.Idle -> currentFile.value != null
                else -> false
            }
        }.map {
            listOf(
                MenuContract.Item.Popup(
                    menuPopup = MenuPopup.Rename,
                    isEnabled = it
                )
            )
        }
    }
}
