package com.omar.retromp3recorder.app.screens.main.components.menu.popups.rename

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.screens.main.components.menu.popups.common.FileNameContentLayout
import com.omar.retromp3recorder.app.screens.main.components.menu.popups.common.rememberFileNameInputState
import com.omar.retromp3recorder.app.screens.main.components.menu.views.PopupButtonData
import com.omar.retromp3recorder.app.screens.main.components.menu.views.PopupComposable
import com.omar.retromp3recorder.app.utils.toFileName

@Composable
fun RenamePopupLayout(
    viewModel: RenameFileViewModelFlow = hiltViewModel(),
    onDismiss: () -> Unit
) {
    val state by viewModel.state.collectAsState(initial = RenameFileContract.State())
    val name =
        state.fileWrapper?.path?.toFileName() ?: ""
    val onValueChanged: (String) -> Unit = {
        viewModel.onEvent(RenameFileContract.Input.CheckCanRename(newName = it))
    }
    if (state.dismiss) {
        SideEffect {
            onDismiss.invoke()
        }
    }

    val nameState = rememberFileNameInputState(name = name)

    PopupComposable(
        title = stringResource(id = R.string.popup_title_rename),
        buttonList = listOf(
            PopupButtonData(isEnabled = state.isOkButtonEnabled,
                text = stringResource(id = R.string.yes),
                onClick = {
                    viewModel.onEvent(
                        RenameFileContract.Input.Rename(
                            newName = state.newName!!
                        )
                    )
                }),
        ),
        content = {
            FileNameContentLayout(
                state = nameState,
                onValueChanged = onValueChanged,
                isError = state.isOkButtonEnabled.not()
            )
        },
        onDismiss = onDismiss
    )
}
