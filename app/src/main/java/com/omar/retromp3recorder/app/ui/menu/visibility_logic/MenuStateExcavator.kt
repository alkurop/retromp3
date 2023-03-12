package com.omar.retromp3recorder.app.ui.menu.visibility_logic

import com.omar.retromp3recorder.app.ui.menu.MenuContract
import com.omar.retromp3recorder.app.ui.menu.visibility_logic.merged.FileActionsStateMapper
import com.omar.retromp3recorder.bl.audio.AudioState
import com.omar.retromp3recorder.bl.audio.AudioStateMapper
import com.omar.retromp3recorder.domain.MenuPopup
import com.omar.retromp3recorder.domain.VisibilityEnabler
import com.omar.retromp3recorder.storage.repo.local.CurrentFileRepo
import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class MenuStateExcavator @Inject constructor(
    private val playerControlsRepo: PlayerControlsRepo,
    private val audioStateMapper: AudioStateMapper,
    private val currentFileRepo: CurrentFileRepo,
    private val fileActionsStateMapper: FileActionsStateMapper
) {
    fun observe(): Observable<MenuContract.State> = currentFileRepo.observe().flatMap { file ->
        Observable
            .combineLatest(
                Observable.combineLatest(
                    listOf(
                        playerControlsRepo.observe()
                            .map { (_, range, _, _) ->
                                listOfNotNull(
                                    MenuContract.Item.Enable(
                                        VisibilityEnabler.RangeBar,
                                        range.isVisible
                                    ),
                                    MenuContract.Item.Popup(
                                        MenuPopup.Crop,
                                        range.isVisible
                                    ),
                                    MenuContract.Item.Popup(
                                        MenuPopup.Search,
                                        file.value?.path != null
                                    )
                                )
                            },
                        fileActionsStateMapper.observe()
                    ),
                ) { array -> array.mapToMenuList() },
                audioStateMapper.observe()
            ) { menu, audioState ->
                MenuContract.State(
                    items = menu,
                    isVisible = audioState != AudioState.Recording,
                )
            }
    }
}
