package com.omar.retromp3recorder.app.ui.log

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.omar.retromp3recorder.app.R


class LogAdapter : RecyclerView.Adapter<LogAdapter.LogViewHolder>() {

    var items: List<LogView.Output> = emptyList()
        set(newValue) {
            field = newValue
            notifyDataSetChanged()
        }

    private lateinit var layoutInflater: LayoutInflater

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        layoutInflater = LayoutInflater.from(recyclerView.context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogViewHolder {
        val view = layoutInflater.inflate(R.layout.log_item_view, parent, false)
        return when (viewType) {
            VIEW_TYPE_MESSAGE -> LogViewHolder.Message(view)
            VIEW_TYPE_ERROR -> LogViewHolder.Error(view)
            else -> error("Unknown view type $viewType")
        }
    }

    override fun onBindViewHolder(holder: LogViewHolder, position: Int) {
        when (holder) {
            is LogViewHolder.Message -> holder.bind(items[position] as LogView.Output.MessageLogOutput)
            is LogViewHolder.Error -> holder.bind(items[position] as LogView.Output.ErrorLogOutput)
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int =
        when (items[position]) {
            is LogView.Output.MessageLogOutput -> VIEW_TYPE_MESSAGE
            is LogView.Output.ErrorLogOutput -> VIEW_TYPE_ERROR
        }

    sealed class LogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        protected val textView: TextView
            get() = itemView.findViewById(R.id.log_text_view)

        protected val context: Context
            get() = itemView.context

        class Message(itemView: View) : LogViewHolder(itemView) {
            fun bind(message: LogView.Output.MessageLogOutput) {
                textView.text = message.message.bell(context)
                textView.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.log_text_color
                    )
                )
            }
        }

        class Error(itemView: View) : LogViewHolder(itemView) {
            fun bind(message: LogView.Output.ErrorLogOutput) {
                val error = message.error.bell(context)
                textView.text = context.getString(
                    R.string.error_string,
                    error
                )
                textView.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.orange
                    )
                )
            }
        }
    }

    class MyDiffCallback(
        private val newList: List<LogView.Output>,
        private val oldList: List<LogView.Output>
    ) :
        DiffUtil.Callback() {
        override fun getOldListSize(): Int {
            return oldList.size
        }

        override fun getNewListSize(): Int {
            return newList.size
        }

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] === newList[newItemPosition]
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            return oldList[oldItemPosition] == newList[newItemPosition]
        }
    }
}

private const val VIEW_TYPE_MESSAGE = 0
private const val VIEW_TYPE_ERROR = 1
