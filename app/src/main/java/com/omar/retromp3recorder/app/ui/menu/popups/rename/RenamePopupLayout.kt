package com.omar.retromp3recorder.app.ui.menu.popups.rename

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.menu.popups.common.FileNameContentLayout
import com.omar.retromp3recorder.app.ui.menu.views.PopupButtonData
import com.omar.retromp3recorder.app.ui.menu.views.PopupComposable
import com.omar.retromp3recorder.app.ui.utils.toFileName

@Composable
fun RenamePopupLayout(viewModel: RenameFileViewModel = viewModel()) {
    val state by viewModel.state.subscribeAsState(initial = RenameFileContract.State())
    val onDismiss = { viewModel.input.onNext(RenameFileContract.Input.DismissPopup) }
    val name =
        state.fileWrapper?.path?.toFileName() ?: ""
    val onValueChanged: (String) -> Unit = {
        viewModel.input.onNext(RenameFileContract.Input.CheckCanRename(newName = it))
    }
    PopupComposable(
        title = stringResource(id = R.string.popup_title_rename),
        content = {
            FileNameContentLayout(
                value = name,
                onValueChanged = onValueChanged,
                isError = state.isOkButtonEnabled.not()
            )
        },
        onDismiss = onDismiss, buttonList = listOf(
            PopupButtonData(isEnabled = state.isOkButtonEnabled,
                text = stringResource(id = R.string.yes),
                onClick = {
                    viewModel.input.onNext(
                        RenameFileContract.Input.Rename(
                            newName = state.newName!!
                        )
                    )
                }),
        )
    )
}
