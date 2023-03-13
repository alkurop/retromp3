package com.omar.retromp3recorder.app.screens.settings

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omar.retromp3recorder.app.BuildConfig
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.screens.settings.components.audio_source.AudioSourceLayout
import com.omar.retromp3recorder.app.screens.settings.components.beat_rate.BitRateLayout
import com.omar.retromp3recorder.app.screens.settings.components.components.checkbox_group.CheckboxGroup
import com.omar.retromp3recorder.app.screens.settings.components.components.checkbox_group.CheckboxGroupData
import com.omar.retromp3recorder.app.screens.settings.components.sample_rate.SampleRateLayout
import com.omar.retromp3recorder.app.screens.settings.flow.SettingsViewModelFlow
import com.omar.retromp3recorder.domain.FeatureFlag
import com.omar.retromp3recorder.domain.FeatureFlagSetting
import com.omar.retromp3recorder.domain.FeatureFlagsCollection
import com.omar.retromp3recorder.domain.FeatureLevel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsLayout(
    onBack: () -> Unit,
    viewModel: SettingsViewModelFlow = viewModel(),
) {
    val state by viewModel.state.collectAsState()

    val featuremap = state.featureFlagsCollection

    val debugflags =
        (if (BuildConfig.DEBUG) featuremap.filterLevel(FeatureLevel.Debug) else emptyMap()).toFlagList()

    val experimentalFlags = featuremap.filterLevel(FeatureLevel.Experimental).toFlagList()

    val productionFlags = featuremap.filterLevel(FeatureLevel.Production).toFlagList()

    val onFlagChanged: (FeatureFlag, Boolean) -> Unit = remember {
        { flag, checked ->
            viewModel.onEvent(
                SettingsContract.Input.FlagSettingChanged(
                    flag,
                    FeatureFlagSetting(checked)
                )
            )
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = stringResource(id = R.string.settings))
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = stringResource(id = R.string.back),
                        )
                    }
                }
            )
        }, content = { padding ->
            LazyColumn(
                modifier = Modifier
                    .padding(padding)
            ) {
                item { BitRateLayout(Modifier.padding(ITEM_PADDING)) }
                item { SampleRateLayout(Modifier.padding(ITEM_PADDING)) }
                item { AudioSourceLayout(Modifier.padding(ITEM_PADDING)) }
                item {
                    CheckboxGroup(
                        modifier = Modifier.padding(ITEM_PADDING),
                        settings = CheckboxGroupData(
                            "",
                            items = productionFlags.toCheckboxItems()
                        ),
                        onItemSelected = { index, checked ->
                            onFlagChanged(productionFlags[index].first, checked)
                        }
                    )
                }
                item {
                    CheckboxGroup(
                        modifier = Modifier.padding(ITEM_PADDING),
                        settings = CheckboxGroupData(
                            stringResource(id = R.string.debug_features),
                            items = debugflags.toCheckboxItems()
                        ),
                        onItemSelected = { index, checked ->
                            onFlagChanged(debugflags[index].first, checked)
                        },
                    )
                }
                item {
                    CheckboxGroup(
                        modifier = Modifier.padding(ITEM_PADDING),
                        settings = CheckboxGroupData(
                            stringResource(id = R.string.experimental_features),
                            items = experimentalFlags.toCheckboxItems()
                        ),
                        onItemSelected = { index, checked ->
                            onFlagChanged(experimentalFlags[index].first, checked)
                        }
                    )
                }
            }
        }
    )
}

private val ITEM_PADDING = 16.dp

@Composable
private fun FeatureFlag.getDisplayName(): String {
    val id = when (this) {
        FeatureFlag.LogView -> R.string.feature_name_log_view
        FeatureFlag.KeepScreenOn -> R.string.feature_name_keep_screen_on
        else -> 0
    }
    return stringResource(id = id)
}

private fun FeatureFlagsCollection?.filterLevel(level: FeatureLevel): Map<FeatureFlag, FeatureFlagSetting> {
    return this?.featuresMap?.filter { it.key.featureLevel == level } ?: emptyMap()
}

private fun Map<FeatureFlag, FeatureFlagSetting>.toFlagList(): List<Pair<FeatureFlag, Boolean>> {
    return this.entries.map { (key, data) ->
        key to data.isEnabled
    }
}

@Composable
private fun List<Pair<FeatureFlag, Boolean>>.toCheckboxItems(): List<CheckboxGroupData.Item> {
    return this.map { CheckboxGroupData.Item(it.first.getDisplayName(), it.second) }
}
