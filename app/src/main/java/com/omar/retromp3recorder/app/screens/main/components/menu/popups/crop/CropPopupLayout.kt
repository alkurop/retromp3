package com.omar.retromp3recorder.app.screens.main.components.menu.popups.crop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.screens.main.components.menu.popups.common.FileNameContentLayout
import com.omar.retromp3recorder.app.screens.main.components.menu.views.PopupButtonData
import com.omar.retromp3recorder.app.screens.main.components.menu.views.PopupComposable
import com.omar.retromp3recorder.utils.domain.updateName

@Composable
fun CropPopupLayout(
    viewModel: CropViewModelFlow = viewModel(),
    onDismiss: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val onValueChanged: (String) -> Unit =
        {
            val newNameSuggestion = (state.nameSuggestion to it).updateName()
            viewModel.emit(CropContract.Input.CheckCanCrop(newNameSuggestion))
        }

    if (state.dismiss) {
        SideEffect {
            onDismiss.invoke()
        }
    }

    PopupComposable(
        title = stringResource(id = R.string.popup_title_crop),
        content = {
            FileNameContentLayout(
                value = state.nameSuggestion.name,
                onValueChanged = onValueChanged,
                isError = state.isOkEnabled.not()
            )
        },
        onDismiss = onDismiss,
        buttonList = listOf(
            PopupButtonData(isEnabled = state.isOkEnabled,
                text = stringResource(id = R.string.popup_button_crop_in_place),
                onClick = { viewModel.emit(CropContract.Input.CropInPlace(state.nameSuggestion)) }),
            PopupButtonData(isEnabled = state.isOkEnabled,
                text = stringResource(id = R.string.popup_button_crop_outside),
                onClick = { viewModel.emit(CropContract.Input.CropOutside(state.nameSuggestion)) })
        )
    )
}
