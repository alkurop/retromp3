package com.omar.retromp3recorder.app.ui.menu.popups.delete

import androidx.compose.runtime.Composable

@Composable
fun DeletePopupLayout (){
}

//private val viewModel by viewModels<DeleteFileViewModel>()
//private lateinit var dialog: AlertDialog
//override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
//    dialog = AlertDialog.Builder(requireContext())
//        .setTitle(getString(R.string.delete_file))
//        .setMessage("fileName")
//        .setPositiveButton(getString(R.string.yes)) { _, _ ->
//        }
//        .setNegativeButton(getString(R.string.no)) { _, _ -> dismiss() }.create()
//    viewModel.state.observe(this, ::render)
//    return dialog
//}
//
//private fun render(state: DeleteFileView.State) {
//    state.fileWrapper?.let {
//        dialog.setMessage(it.path.toFileName())
//    }
//    dialog.getButton(DialogInterface.BUTTON_POSITIVE).setOnClickListener {
//        viewModel.input.onNext(DeleteFileView.Input.DeleteFile)
//    }
//    state.shouldDismiss.takeIf { it }?.let { dismiss() }
//}