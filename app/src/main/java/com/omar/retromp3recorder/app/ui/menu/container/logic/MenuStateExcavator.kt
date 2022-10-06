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
    fun observe(): Observable<MenuView.State> = Observable
        .combineLatest(
            playerControlsRepo.observe()
                .map { (_, range, _, _) ->
                    listOfNotNull(
                        MenuView.Item.Enable(
                            VisibilityEnabler.RangeBar,
                            range.isVisible
                        ),
                        MenuView.Item.Popup(
                            MenuExecutable.Crop,
                            range.isVisible
                        )
                    )

                },
            menuPopupBus.observe(),
            audioStateMapper.observe()
        ) { menu, popup, audioState ->
            MenuView.State(
                items = menu,
                isVisible = audioState != AudioState.Recording,
                popup = popup
            )
        }
}
