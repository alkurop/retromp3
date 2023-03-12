package com.omar.retromp3recorder.app.screens.main.components.menu.visibility_logic.file

import com.omar.retromp3recorder.app.screens.main.components.menu.MenuContract
import com.omar.retromp3recorder.app.screens.main.components.menu.visibility_logic.MenuVisibilityMapper
import com.omar.retromp3recorder.bl.audio.AudioState
import com.omar.retromp3recorder.bl.audio.AudioStateMapper
import com.omar.retromp3recorder.domain.MenuPopup
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class RenameFileMenuStateMapper @Inject constructor(
    private val audioStateMapper: AudioStateMapper, private val currentFileRepo: CurrentFileRepo
) : MenuVisibilityMapper {
    override fun observe(): Observable<List<MenuContract.Item>> {
        return Observable.combineLatest(
            currentFileRepo.observe(),
            audioStateMapper.observe()
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
