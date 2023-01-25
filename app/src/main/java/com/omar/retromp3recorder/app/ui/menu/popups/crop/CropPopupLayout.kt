package com.omar.retromp3recorder.app.ui.menu.popups.crop

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.menu.popups.common.FileNameContentLayout
import com.omar.retromp3recorder.app.ui.menu.views.PopupButtonData
import com.omar.retromp3recorder.app.ui.menu.views.PopupComposable
import com.omar.retromp3recorder.utils.updateName

@Composable
fun CropPopupLayout(viewModel: CropViewModel = viewModel()) {
    val state by viewModel.state.subscribeAsState(initial = CropContract.State())
    val onDismiss = { viewModel.input.onNext(CropContract.Input.DismissPopup) }
    val onValueChanged: (String) -> Unit =
        {
            val newNameSuggestion = (state.nameSuggestion to it).updateName()
            viewModel.input.onNext(CropContract.Input.CheckCanCrop(newNameSuggestion))
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
                onClick = { viewModel.input.onNext(CropContract.Input.CropInPlace(state.nameSuggestion)) }),
            PopupButtonData(isEnabled = state.isOkEnabled,
                text = stringResource(id = R.string.popup_button_crop_outside),
                onClick = { viewModel.input.onNext(CropContract.Input.CropOutside(state.nameSuggestion)) })
        )
    )
}
