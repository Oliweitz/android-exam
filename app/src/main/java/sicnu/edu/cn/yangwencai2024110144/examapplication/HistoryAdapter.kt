package sicnu.edu.cn.yangwencai2024110144.examapplication

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import sicnu.edu.cn.yangwencai2024110144.examapplication.databinding.ItemHistoryBinding

/**
 * 将计算历史绑定到 RecyclerView。
 */
class HistoryAdapter : ListAdapter<HistoryRecord, HistoryAdapter.HistoryViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class HistoryViewHolder(
        private val binding: ItemHistoryBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(record: HistoryRecord) {
            binding.textHistoryEntry.text = binding.root.context.getString(
                R.string.history_entry_format,
                record.expression,
                record.result
            )
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<HistoryRecord>() {
        override fun areItemsTheSame(oldItem: HistoryRecord, newItem: HistoryRecord): Boolean {
            return oldItem.timestamp == newItem.timestamp &&
                oldItem.expression == newItem.expression
        }

        override fun areContentsTheSame(oldItem: HistoryRecord, newItem: HistoryRecord): Boolean {
            return oldItem == newItem
        }
    }
}
