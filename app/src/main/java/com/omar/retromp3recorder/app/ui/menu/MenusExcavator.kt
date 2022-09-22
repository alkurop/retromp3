package com.omar.retromp3recorder.app.ui.menu

import com.omar.retromp3recorder.dto.FeatureFlag
import com.omar.retromp3recorder.storage.repo.global.FeatureFlagRepo
import com.omar.retromp3recorder.storage.repo.local.PlayerControlsRepo
import io.reactivex.rxjava3.core.Observable
import javax.inject.Inject

class MenusExcavator @Inject constructor(
    private val playerControlsRepo: PlayerControlsRepo,
    private val featureFlagRepo: FeatureFlagRepo
) {
    fun observable(): Observable<MenuView.State> =
        playerControlsRepo
            .observe(
            ).map { (range, loop, reverse) ->
//todo add real actions
                MenuView.State(
                    isVisible = true,
                    actions = listOf()
                )
            }
            .zipWith(featureFlagRepo.observe()) { menu, featureFlags ->
                menu.copy(isVisible = featureFlags.isEnabled(FeatureFlag.MenuView))
            }

}
