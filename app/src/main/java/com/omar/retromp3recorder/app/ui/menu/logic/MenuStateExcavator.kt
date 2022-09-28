package com.omar.retromp3recorder.app.ui.menu.logic

import com.omar.retromp3recorder.dto.FeatureFlag
import com.omar.retromp3recorder.dto.MenuExecutable
import com.omar.retromp3recorder.dto.VisibilityEnabler
import com.omar.retromp3recorder.storage.repo.global.FeatureFlagRepo
import com.omar.retromp3recorder.storage.repo.local.MenuPopupBus
import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class MenuStateExcavator @Inject constructor(
    private val playerControlsRepo: PlayerControlsRepo,
    private val featureFlagRepo: FeatureFlagRepo,
    private val menuPopupBus: MenuPopupBus
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
            featureFlagRepo.observe(),
            menuPopupBus.observe()
        ) { menu, featureFlags, popup ->
            MenuView.State(
                items = menu,
                isVisible = featureFlags.isEnabled(FeatureFlag.MenuView),
                popup = popup
            )
        }
}
