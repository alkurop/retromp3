package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.app.MediaProjectionService
import com.omar.retromp3recorder.app.screens.main.MainViewModel
import com.omar.retromp3recorder.app.screens.main.components.audio_controls.AudioControlsViewModel
import com.omar.retromp3recorder.app.screens.main.components.joined_progress.JoinedProgressViewModel
import com.omar.retromp3recorder.app.screens.main.components.log.LogViewModel
import com.omar.retromp3recorder.app.screens.main.components.menu.popups.crop.CropViewModel
import com.omar.retromp3recorder.app.screens.main.components.menu.popups.delete.DeleteFileViewModel
import com.omar.retromp3recorder.app.screens.main.components.menu.popups.rename.RenameFileViewModel
import com.omar.retromp3recorder.app.screens.main.components.menu.views.MenuViewModel
import com.omar.retromp3recorder.app.screens.main.components.rangebar.RangeBarViewModel
import com.omar.retromp3recorder.app.screens.main.components.visualizer.VisualizerViewModel
import com.omar.retromp3recorder.app.screens.search.SelectorViewModel
import com.omar.retromp3recorder.app.screens.settings.components.audio_source.AudioSourceViewModel
import com.omar.retromp3recorder.app.screens.settings.components.beat_rate.BitRateSettingsViewModel
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
    abstract fun inject(bitRateSettingsViewModel: BitRateSettingsViewModel)
    abstract fun inject(audioSourceViewModel: AudioSourceViewModel)
    abstract fun inject(settingsViewModelFlow: SettingsViewModelFlow)
    abstract fun inject(sampleRateViewModelFlow: SampleRateViewModelFlow)
}


@Track
@Subcomponent(
    modules = [ScopedFunctionalityModule::class]
)
interface TrackComponent {
    fun inject(viewModel: AudioControlsViewModel)
    fun inject(mainViewModel: MainViewModel)
    fun inject(visualizerViewModel: VisualizerViewModel)
    fun inject(fileSelectorViewModel: SelectorViewModel)
    fun inject(deleteFileViewModel: DeleteFileViewModel)
    fun inject(renameFileViewModel: RenameFileViewModel)
    fun inject(joinedProgressViewModel: JoinedProgressViewModel)
    fun inject(rangeBarViewModel: RangeBarViewModel)
    fun inject(menuViewModel: MenuViewModel)
    fun inject(cropViewModel: CropViewModel)
}
