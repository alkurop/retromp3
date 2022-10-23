package com.omar.retromp3recorder.app.ui.menu.container.logic

import com.omar.retromp3recorder.bl.audio.AudioState
import com.omar.retromp3recorder.bl.audio.AudioStateMapper
import com.omar.retromp3recorder.dto.MenuExecutable
import com.omar.retromp3recorder.dto.VisibilityEnabler
import com.omar.retromp3recorder.storage.repo.local.MenuPopupBus
import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class MenuStateExcavator @Inject constructor(
    private val playerControlsRepo: PlayerControlsRepo,
    private val menuPopupBus: MenuPopupBus,
    private val audioStateMapper: AudioStateMapper
) {
    @Suppress("unused")
    fun observe(): Observable<MenuContract.State> = Observable
        .combineLatest(
            playerControlsRepo.observe()
                .map { (_, range, _, _) ->
                    listOfNotNull(
                        MenuContract.Item.Enable(
                            VisibilityEnabler.RangeBar,
                            range.isVisible
                        ),
                        MenuContract.Item.Popup(
                            MenuExecutable.Crop,
                            range.isVisible
                        ),
                        MenuContract.Item.Popup(
                            MenuExecutable.Search,
                            true
                        )
                    )
                },
            menuPopupBus.observe(),
            audioStateMapper.observe()
        ) { menu, popup, audioState ->
            MenuContract.State(
                items = menu,
                isVisible = audioState != AudioState.Recording,
                popup = popup
            )
        }
}
