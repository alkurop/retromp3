package com.omar.retromp3recorder.app.ui.rangebar

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.github.alkurop.rangebar.RangeBar
import com.jakewharton.rxbinding4.view.clicks
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.utils.lazyView
import com.omar.retromp3recorder.app.uiutils.TimeDisplay.toDisplay
import com.omar.retromp3recorder.app.uiutils.observe
import com.omar.retromp3recorder.dto.PlayerRange

class RangeBarFragment : Fragment(R.layout.fragment_rangebar) {
    private val rangeBar by lazyView<RangeBar>(R.id.range_bar)
    private val rangeBarStateText by lazyView<TextView>(R.id.range_state)
    private val startView by lazyView<TextView>(R.id.range_start)
    private val endView by lazyView<TextView>(R.id.range_end)
    private val viewModel: RangeBarViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.state.observe(viewLifecycleOwner, ::renderState)
        rangeBarStateText.clicks().observe(viewLifecycleOwner) {
            viewModel.input.onNext(RangeBarView.Input.Enable)
        }
        rangeBar.setOnRangeBarChangeListener { tick ->
            viewModel.input.onNext(
                RangeBarView.Input.RangeSet(
                    PlayerRange(
                        tick.start,
                        tick.end,
                        tick.range
                    )
                )
            )
        }
    }

    private fun renderState(state: RangeBarView.State) {
        when (state) {
            RangeBarView.State.Hidden -> {
                view?.isGone = true
            }
            is RangeBarView.State.Visible -> {
                view?.isVisible = true
                startView.text = state.fromMillis.toDisplay(requireContext())
                endView.text = state.toMillis.toDisplay(requireContext())
                val active = state.isActive
                rangeBar.isActivated = active
                val rangeState = getString(if (active) R.string.on else R.string.off)
                rangeBarStateText.text = getString(R.string.range_enabled, rangeState)
            }
        }
    }
}
