package com.omar.retromp3recorder.app.screens.main.components.menu.popups.delete

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.screens.main.components.menu.views.PopupButtonData
import com.omar.retromp3recorder.app.screens.main.components.menu.views.PopupComposable
import com.omar.retromp3recorder.app.utils.toFileName

@Composable
fun DeletePopupLayout(viewModel: DeleteFileViewModelFlow = hiltViewModel(), onDismiss: () -> Unit) {
    val state by viewModel.state.collectAsState()
    if (state.shouldDismiss) {
        SideEffect {
            onDismiss.invoke()
        }
    }
    PopupComposable(
        title = stringResource(id = R.string.popup_title_delete),
        buttonList = listOf(
            PopupButtonData(isEnabled = true,
                text = stringResource(id = R.string.yes),
                onClick = { viewModel.emit(DeleteFileContract.Input.DeleteFile) }),
        ),
        content = {
            Text(state.fileWrapper?.path?.toFileName() ?: "")
        },
        onDismiss = onDismiss,
    )
}
