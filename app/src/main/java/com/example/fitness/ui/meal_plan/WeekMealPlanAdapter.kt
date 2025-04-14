package com.example.fitness.ui.meal_plan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.fitness.R
import com.example.fitness.data.model.Tag
import com.example.fitness.data.model.Week
import com.example.fitness.databinding.LayoutItemCalendarBinding
import com.example.fitness.databinding.LayoutItemCalendarMealPlanBinding
import com.example.fitness.databinding.LayoutItemTagBinding
import com.example.fitness.util.base.BaseAdapter

class WeekMealPlanAdapter(
    private val listener: (Week) -> Unit = {},
    private val currentDate: Int = 0
) : BaseAdapter<Week, LayoutItemCalendarMealPlanBinding>(DIFF_WEEK) {

    private var selectedPosition = currentDate
    private var mWeek: Week? = null

    companion object {
        val DIFF_WEEK = object : DiffUtil.ItemCallback<Week>() {
            override fun areItemsTheSame(oldItem: Week, newItem: Week): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Week, newItem: Week): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun createBinding(parent: ViewGroup): LayoutItemCalendarMealPlanBinding {
        val inflater = LayoutInflater.from(parent.context)
        return LayoutItemCalendarMealPlanBinding.inflate(inflater, parent, false)
    }

    override fun bind(binding: LayoutItemCalendarMealPlanBinding, item: Week, position: Int) {
        binding.apply {
            mWeek = item
            tvDay.text = item.date
            tvDayOfWeek.text = item.dayOfWeek

            root.setBackgroundResource(
                if (position == selectedPosition) R.drawable.bg_btn_meal_plan
                else R.drawable.bg_btn_meal_plan_gray
            )

            tvDay.setTextColor(
                if (position == selectedPosition) root.context.getColor(R.color.black_green)
                else root.context.getColor(R.color.black)
            )

            tvDayOfWeek.setTextColor(
                if (position == selectedPosition) root.context.getColor(R.color.white)
                else root.context.getColor(R.color.gray)
            )

            root.setOnClickListener {
                val previousSelected = selectedPosition
                selectedPosition = position

                notifyItemChanged(previousSelected)
                notifyItemChanged(selectedPosition)

                listener.invoke(item)
            }

        }
    }

    fun setSelectedPosition(position: Int) {
        selectedPosition = position
        mWeek?.let { listener.invoke(it) }
    }
}