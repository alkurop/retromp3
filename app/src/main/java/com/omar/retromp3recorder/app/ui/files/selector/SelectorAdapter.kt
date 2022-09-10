package com.omar.retromp3recorder.app.ui.files.selector

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.omar.retromp3recorder.app.R
import com.omar.retromp3recorder.app.ui.utils.findViewById
import com.omar.retromp3recorder.app.ui.utils.toFileName
import com.omar.retromp3recorder.app.uiutils.TimeDisplay.toDisplay
import com.omar.retromp3recorder.ui.wavetable.WavetablePreview

class SelectorAdapter(
    val onItemSelectedListener: (SelectorView.Item) -> Unit
) : RecyclerView.Adapter<SelectorAdapter.SelectorViewHolder>() {
    private lateinit var layoutInflater: LayoutInflater
    var items: List<SelectorView.Item> = emptyList()
        set(value) {
            val diff = DiffUtil.calculateDiff(
                Diff(field, value)
            )
            field = value
            diff.dispatchUpdatesTo(this)
        }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        layoutInflater = LayoutInflater.from(recyclerView.context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectorViewHolder {
        val view = layoutInflater.inflate(R.layout.selector_item_view, parent, false)
        return SelectorViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectorViewHolder, position: Int) {
        holder.bind(items[position])
    }

    override fun getItemCount(): Int = items.size

    inner class SelectorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val currentFileSign: View
            get() = findViewById(R.id.current_file_sign)
        private val textView: TextView
            get() = findViewById(R.id.current_file_text)
        private val timeView: TextView
            get() = findViewById(R.id.time)
        private val wavetablePreview: WavetablePreview
            get() = findViewById(R.id.wavetable)

        fun bind(item: SelectorView.Item) {
            itemView.setOnClickListener { onItemSelectedListener(item) }
            textView.text = item.fileWrapper.path.toFileName()
            currentFileSign.isVisible = item.isCurrentItem
            val wavetable = item.fileWrapper.wavetable
            wavetablePreview.isVisible = wavetable != null
            if (wavetable != null) {
                wavetablePreview.isVisible = true
                wavetablePreview.update(wavetable.data)
            } else {
                wavetablePreview.isVisible = false
            }
            timeView.text = item.fileWrapper.length?.toDisplay(itemView.context)
        }
    }
}

private class Diff(
    private val oldList: List<SelectorView.Item>,
    private val newList: List<SelectorView.Item>
) : DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        return oldItem.fileWrapper.createTimedStamp == newItem.fileWrapper.createTimedStamp
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldList[oldItemPosition]
        val newItem = newList[newItemPosition]
        val isFilepathSame = oldItem.fileWrapper.path == newItem.fileWrapper.path
        val isSelectionSame = oldItem.isCurrentItem == newItem.isCurrentItem
        return isFilepathSame && isSelectionSame
    }
}