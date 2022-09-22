package com.omar.retromp3recorder.app.ui.menu

import com.omar.retromp3recorder.storage.repo.local.PlayerFeaturesRepo
import javax.inject.Inject

class MenuEnablersMapper @Inject constructor(
    private val playerFeaturesRepo: PlayerFeaturesRepo
) {
}
