package com.omar.retromp3recorder.app.ui.recorder_settings

import android.annotation.SuppressLint
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.utils.lazyView

abstract class RecorderSettingsBaseFragment : Fragment(R.layout.fragment_recorder_controls) {
    private val checkboxHeight by lazy { resources.getDimensionPixelSize(R.dimen.cb_height) }
    private val container by lazyView<ViewGroup>(R.id.container)

    protected fun addTitleView(title: String) {
        layoutInflater.inflate(R.layout.container_title, container)
        val titleView = container.findViewById<TextView>(R.id.title)
        titleView.text = title
    }

    @SuppressLint("InflateParams")
    protected fun addCheckBox(title: String): RadioButton {
        return (layoutInflater.inflate(R.layout.radio_button, null) as RadioButton).apply {
            this.text = title
            this.height = checkboxHeight
            container.addView(this)
        }
    }
}