package com.example.fitness.ui.reminder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.fitness.data.model.Reminder
import com.example.fitness.databinding.LayoutItemReminderBinding
import com.example.fitness.util.FormatTime.formatDateTime
import com.example.fitness.util.FormatTime.formatToHourMinute
import com.example.fitness.util.base.BaseAdapter

class ReminderAdapter(
    private val onClickDelete: (Reminder) -> Unit = {},
    private val onClickUpdate: (Reminder) -> Unit = {}
) : BaseAdapter<Reminder, LayoutItemReminderBinding>(DIFF_REMINDER) {

    companion object {
        val DIFF_REMINDER = object : DiffUtil.ItemCallback<Reminder>() {
            override fun areItemsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Reminder, newItem: Reminder): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun createBinding(parent: ViewGroup): LayoutItemReminderBinding {
        val inflater = LayoutInflater.from(parent.context)
        return LayoutItemReminderBinding.inflate(inflater, parent, false)
    }

    override fun bind(binding: LayoutItemReminderBinding, item: Reminder, position: Int) {
        binding.tvTitleReminder.text = item.title
        binding.tvTimeReminder.text = item.time?.let { formatDateTime(it) }
        binding.tvTime1.text = item.time?.let { formatToHourMinute(it) }
        binding.ivDeleteReminder.setOnClickListener { onClickDelete.invoke(item) }
        binding.root.setOnClickListener { onClickUpdate.invoke(item) }
    }
}