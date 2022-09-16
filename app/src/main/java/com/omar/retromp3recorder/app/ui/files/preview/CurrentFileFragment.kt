package com.omar.retromp3recorder.app.ui.files.preview

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.files.CurrentFileActivity
import com.omar.retromp3recorder.app.ui.files.delete.DeleteFileDialogFragment
import com.omar.retromp3recorder.app.ui.files.rename.RenameFileDialogFragment
import com.omar.retromp3recorder.app.ui.utils.lazyView
import com.omar.retromp3recorder.app.ui.utils.toFileName
import com.omar.retromp3recorder.app.uiutils.observe

class CurrentFileFragment : Fragment(R.layout.fragment_current_file) {
    private val textView by lazyView<TextView>(R.id.current_file_text)
    private val buttonOpen by lazyView<View>(R.id.button_open)
    private val buttonDelete by lazyView<View>(R.id.button_delete)
    private val viewModel: CurrentFileViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::renderState)
        buttonOpen.setOnClickListener {
            startActivity(Intent(requireContext(), CurrentFileActivity::class.java))
        }
        buttonDelete.setOnClickListener {
            DeleteFileDialogFragment().show(
                childFragmentManager,
                DeleteFileDialogFragment::class.java.canonicalName
            )
        }
        textView.setOnClickListener {
            RenameFileDialogFragment().show(
                childFragmentManager,
                RenameFileDialogFragment::class.java.canonicalName
            )
        }
    }

    private fun renderState(state: CurrentFileView.State) {
        textView.text = state.filePath?.toFileName() ?: getString(R.string.please_record_something)
        textView.isClickable = state.isRenameButtonActive
        buttonOpen.setIsButtonActive(state.isOpenFileActive)
        buttonDelete.setIsButtonActive(state.isDeleteFileActive)
    }
}

private fun View.setIsButtonActive(isVisible: Boolean) {
    this.isClickable = isVisible
    this.alpha = if (isVisible) 1.0f else 0.4f
}
