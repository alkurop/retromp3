package com.omar.retromp3recorder.app.di

import com.omar.retromp3recorder.app.MediaProjectionService
import com.omar.retromp3recorder.app.ui.audio_controls.AudioControlsViewModel
import com.omar.retromp3recorder.app.ui.files.selector.SelectorViewModel
import com.omar.retromp3recorder.app.ui.joined_progress.JoinedProgressViewModel
import com.omar.retromp3recorder.app.ui.log.LogViewModel
import com.omar.retromp3recorder.app.ui.main.MainViewModel
import com.omar.retromp3recorder.app.ui.menu.popups.crop.CropViewModel
import com.omar.retromp3recorder.app.ui.menu.popups.delete.DeleteFileViewModel
import com.omar.retromp3recorder.app.ui.menu.popups.rename.RenameFileViewModel
import com.omar.retromp3recorder.app.ui.menu.views.MenuViewModel
import com.omar.retromp3recorder.app.ui.rangebar.RangeBarViewModel
import com.omar.retromp3recorder.app.ui.recorder_settings.audio_source.AudioSourceViewModel
import com.omar.retromp3recorder.app.ui.recorder_settings.beat_rate.BitRateSettingsViewModel
import com.omar.retromp3recorder.app.ui.recorder_settings.sample_rate.SampleRateViewModel
import com.omar.retromp3recorder.app.ui.settings.SettingsViewModel
import com.omar.retromp3recorder.app.ui.visualizer.VisualizerViewModel
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
    abstract fun inject(settingsViewModel: SettingsViewModel)
    abstract fun inject(sampleRateViewModel: SampleRateViewModel)
    abstract fun inject(audioSourceViewModel: AudioSourceViewModel)
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
