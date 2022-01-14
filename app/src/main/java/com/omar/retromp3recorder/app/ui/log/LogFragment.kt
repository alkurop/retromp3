package com.omar.retromp3recorder.app.ui.log

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.utils.lazyView
import com.omar.retromp3recorder.app.uiutils.observe

class LogFragment : Fragment(R.layout.fragment_log) {
    private val recyclerView by lazyView<RecyclerView>(R.id.log_holder)
    private val adapter = LogAdapter()
    private val viewModel by viewModels<LogViewModel>()
    private val scrollDownHandler = Handler(Looper.getMainLooper())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        viewModel.state.observe(viewLifecycleOwner, ::renderState)
    }

    private fun renderState(state: LogView.State) {
        state.apply {
            adapter.items = messages
            recyclerView.smoothScrollToPosition(messages.size)
            view?.isVisible = isVisible
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        scrollDownHandler.removeCallbacksAndMessages(null)
    }
}
