package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.app.MediaProjectionService
import com.omar.retromp3recorder.app.screens.main.MainViewModel
import com.omar.retromp3recorder.app.screens.main.components.audio_controls.AudioControlsViewModelFlow
import com.omar.retromp3recorder.app.screens.main.components.joined_progress.JoinedProgressViewModelFlow
import com.omar.retromp3recorder.app.screens.main.components.log.LogViewModel
import com.omar.retromp3recorder.app.screens.main.components.menu.popups.crop.CropViewModel
import com.omar.retromp3recorder.app.screens.main.components.menu.popups.delete.DeleteFileViewModel
import com.omar.retromp3recorder.app.screens.main.components.menu.popups.rename.RenameFileViewModelFlow
import com.omar.retromp3recorder.app.screens.main.components.menu.visibility_logic.MenuViewModelFlow
import com.omar.retromp3recorder.app.screens.main.components.rangebar.RangeBarViewModelFlow
import com.omar.retromp3recorder.app.screens.main.components.visualizer.VisualizerViewModelFlow
import com.omar.retromp3recorder.app.screens.search.SelectorViewModelFlow
import com.omar.retromp3recorder.app.screens.settings.components.audio_source.AudioSourceViewModelFlow
import com.omar.retromp3recorder.app.screens.settings.components.beat_rate.BitRateSettingsViewModelFlow
import com.omar.retromp3recorder.app.screens.settings.components.sample_rate.SampleRateViewModelFlow
import com.omar.retromp3recorder.app.screens.settings.flow.SettingsViewModelFlow
import com.omar.retromp3recorder.storage.StorageModule
import com.omar.retromp3recorder.utils.domain.Track
import dagger.Component
import dagger.Subcomponent
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        AndroidModule::class,
        FunctionalityModule::class,
        ScopedFunctionalityModule::class,
        StorageModule::class
    ]
)
abstract class AppComponent {

    private val componentsMap: MutableMap<String, TrackComponent> = mutableMapOf()

    fun getComponent(key: String = "default"): TrackComponent {
        val component = componentsMap[key] ?: plus()
        componentsMap[key] = component
        return component
    }

    abstract fun plus(): TrackComponent

    abstract fun inject(mediaProjectionService: MediaProjectionService)
    abstract fun inject(logViewModel: LogViewModel)
    abstract fun inject(settingsViewModelFlow: SettingsViewModelFlow)
    abstract fun inject(sampleRateViewModelFlow: SampleRateViewModelFlow)
    abstract fun inject(audioSourceViewModelFlow: AudioSourceViewModelFlow)
    abstract fun inject(bitRateViewModelFlow: BitRateSettingsViewModelFlow)
}


@Track
@Subcomponent(
    modules = [ScopedFunctionalityModule::class]
)
interface TrackComponent {
    fun inject(mainViewModel: MainViewModel)
    fun inject(deleteFileViewModel: DeleteFileViewModel)
    fun inject(cropViewModel: CropViewModel)
    fun inject(audioControlsViewModelFlow: AudioControlsViewModelFlow)
    fun inject(joinedProgressViewModelFlow: JoinedProgressViewModelFlow)
    fun inject(rangeBarViewModelFlow: RangeBarViewModelFlow)
    fun inject(menuViewModelFlow: MenuViewModelFlow)
    fun inject(selectorViewModelFlow: SelectorViewModelFlow)
    fun inject(visualizerViewModel: VisualizerViewModelFlow)
    fun inject(renameFileViewModelFlow: RenameFileViewModelFlow)
}
