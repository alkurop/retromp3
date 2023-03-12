package com.omar.retromp3recorder.app.ui.menu.popups.delete

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rxjava3.subscribeAsState
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.menu.views.PopupButtonData
import com.omar.retromp3recorder.app.ui.menu.views.PopupComposable
import com.omar.retromp3recorder.app.ui.utils.toFileName

@Composable
fun DeletePopupLayout(viewModel: DeleteFileViewModel = viewModel(), onDismiss: () -> Unit) {
    val state by viewModel.state.subscribeAsState(initial = DeleteFileContract.State())
    if (state.shouldDismiss) {
        SideEffect {
            onDismiss.invoke()
        }
    }
    PopupComposable(
        title = stringResource(id = R.string.popup_title_delete),
        content = {
            Text(state.fileWrapper?.path?.toFileName() ?: "")
        },
        onDismiss = onDismiss,
        buttonList = listOf(
            PopupButtonData(isEnabled = true,
                text = stringResource(id = R.string.yes),
                onClick = { viewModel.input.onNext(DeleteFileContract.Input.DeleteFile) }),
        )
    )
}
