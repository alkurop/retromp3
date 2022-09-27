package com.omar.retromp3recorder.app.ui.menu

import com.omar.retromp3recorder.dto.*
import com.omar.retromp3recorder.storage.repo.global.FeatureFlagRepo
import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class MenuStateMapper @Inject constructor(
    private val playerControlsRepo: PlayerControlsRepo,
    private val featureFlagRepo: FeatureFlagRepo
) {
    fun observe(): Observable<MenuView.State> =
        Observable.combineLatest(
            playerControlsRepo
                .observe().map { (loop, range, reverse, speed) ->
                    MenuView.State(
                        items = listOf(
//                            MenuAction.Execute(
//                                MenuExecutable.Crop
//                            ),
                            MenuAction.Enable(
                                VisibilityEnabler.RangeBar,
                                range.isVisible
                            ),
//                            MenuAction.Enable(
//                                AudioEnabler.Loop,
//                                loop.isEnabled
//                            ),
//                            MenuAction.Enable(
//                                AudioEnabler.Reverse,
//                                reverse.isEnabled
//                            ),
//                            MenuAction.Enable(
//                                VisibilityEnabler.PlaybackSpeed,
//                                speed.isEnabled
//                            )
                        )
                    )
                },
            featureFlagRepo.observe()
        ) { menu, featureFlags ->
            menu.copy(isVisible = featureFlags.isEnabled(FeatureFlag.MenuView))
        }
}
