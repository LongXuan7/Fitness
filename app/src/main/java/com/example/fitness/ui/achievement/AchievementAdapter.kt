package com.example.fitness.ui.achievement

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import com.example.fitness.R
import com.example.fitness.data.model.Week
import com.example.fitness.data.model.WorkoutPlan
import com.example.fitness.databinding.LayoutItemAchievementBinding
import com.example.fitness.util.base.BaseAdapter
import java.time.LocalDate

class AchievementAdapter(
    private val listener: (List<WorkoutPlan>) -> Unit = {},
) : BaseAdapter<Week, LayoutItemAchievementBinding>(DIFF_WEEK) {

    private var mWourkoutPlanList: List<WorkoutPlan> = emptyList()

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

    override fun createBinding(parent: ViewGroup): LayoutItemAchievementBinding {
        val inflater = LayoutInflater.from(parent.context)
        return LayoutItemAchievementBinding.inflate(inflater, parent, false)
    }

    @SuppressLint("SetTextI18n")
    override fun bind(binding: LayoutItemAchievementBinding, item: Week, position: Int) {
        binding.apply {
            mWeek = item
            tvDay.text = item.date
            tvDayOfWeek.text = item.dayOfWeek

            val day = item.date.padStart(2, '0')
            val month = item.month.padStart(2, '0')
            val fullDate = "${item.year}-$month-$day"
            val targetDate = LocalDate.parse(fullDate)

            val matchedWorkouts = mWourkoutPlanList.filter { plan ->
                try {
                    val workoutDate = LocalDate.parse(plan.time)

                    workoutDate == targetDate

                } catch (e: Exception) {
                    false
                }
            }


            if (matchedWorkouts.isNotEmpty()) {
                val complete = matchedWorkouts.filter { it.progress == 100 }
                tvComplateSet.text = if (complete.isNotEmpty()) complete.size.toString() else "0"
                tvSet.text = matchedWorkouts.size.toString()

                if (complete.size == matchedWorkouts.size) {
                    binding.tvStatusAr.setTextColor(ContextCompat.getColor(root.context, R.color.light_green))
                    binding.tvStatusAr.text = "Hoàn thành"
                    binding.ivStatusAr.setImageResource(R.drawable.check_circle_green)
                    linearLayout6.setBackgroundResource(R.drawable.bg_calander_black)
                }else {
                    binding.tvStatusAr.setTextColor(
                        ContextCompat.getColor(
                            root.context,
                            R.color.red
                        )
                    )
                    binding.tvStatusAr.text = "Chưa hoàn thành"
                    binding.ivStatusAr.setImageResource(R.drawable.error)
                    linearLayout6.setBackgroundResource(R.drawable.bg_calander_light_black)
                }
                val progressSum = matchedWorkouts.sumOf { it.progress ?: 0 }
                binding.tvPercent.text = "${progressSum / matchedWorkouts.size}%"

            }

            btnNextAr.setOnClickListener {
                listener.invoke(matchedWorkouts)
            }

        }
    }

    fun setData(workoutPlanList: List<WorkoutPlan>) {
        mWourkoutPlanList = workoutPlanList
    }
}