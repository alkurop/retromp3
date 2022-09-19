package com.omar.retromp3recorder.app.ui.files.selector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.utils.findViewById
import com.omar.retromp3recorder.app.ui.utils.toFileName
import com.omar.retromp3recorder.app.uiutils.TimeDisplay.toDisplay
import com.omar.retromp3recorder.dto.ExistingFileWrapper
import com.omar.retromp3recorder.storage.db.FileDbEntity
import com.omar.retromp3recorder.storage.db.toFileWrapper
import com.omar.retromp3recorder.ui.wavetable.BytesWithRange
import com.omar.retromp3recorder.ui.wavetable.WavetablePreview

class SelectorAdapter(
    val onItemSelectedListener: (ExistingFileWrapper) -> Unit,
) : PagedListAdapter<FileDbEntity, SelectorAdapter.SelectorViewHolder>(Diff()) {
    lateinit var currentFile: String
    private lateinit var layoutInflater: LayoutInflater

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        layoutInflater = LayoutInflater.from(recyclerView.context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectorViewHolder {
        val view = layoutInflater.inflate(R.layout.selector_item_view, parent, false)
        return SelectorViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectorViewHolder, position: Int) {
        holder.bind(getItem(position)?.toFileWrapper()!!)
    }

    fun ExistingFileWrapper.isCurrentFile(): Boolean {
        return this.path == currentFile
    }

    inner class SelectorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val currentFileSign: View
            get() = findViewById(R.id.current_file_sign)
        private val textView: TextView
            get() = findViewById(R.id.current_file_text)
        private val timeView: TextView
            get() = findViewById(R.id.time)
        private val wavetablePreview: WavetablePreview
            get() = findViewById(R.id.wavetable)

        fun bind(item: ExistingFileWrapper) {
            itemView.setOnClickListener { onItemSelectedListener(item) }
            textView.text = item.path.toFileName()
            currentFileSign.isVisible = item.isCurrentFile()
            val wavetable = item.wavetable
            wavetablePreview.isVisible = wavetable != null
            if (wavetable != null) {
                wavetablePreview.isVisible = true
                wavetablePreview.update(BytesWithRange(wavetable.data, null))
            } else {
                wavetablePreview.isVisible = false
            }
            timeView.text = item.length?.toDisplay(itemView.context)
        }
    }
}

private class Diff : DiffUtil.ItemCallback<FileDbEntity>() {
    override fun areItemsTheSame(oldItem: FileDbEntity, newItem: FileDbEntity): Boolean {
        return oldItem.toFileWrapper() == newItem.toFileWrapper()
    }

    override fun areContentsTheSame(oldItem: FileDbEntity, newItem: FileDbEntity): Boolean {
        return oldItem.toFileWrapper() == newItem.toFileWrapper()
    }
}
