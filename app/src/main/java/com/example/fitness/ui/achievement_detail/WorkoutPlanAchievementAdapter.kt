package com.example.fitness.ui.achievement_detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.fitness.R
import com.example.fitness.data.model.Category
import com.example.fitness.data.model.Exercise
import com.example.fitness.data.model.WorkoutPlan
import com.example.fitness.databinding.LayoutItemAchievementDetailBinding
import com.example.fitness.util.base.BaseAdapter
import com.example.fitness.util.ext.loadImage

class WorkoutPlanAchievementAdapter(
    private val mCategories: List<Category>,
    private val mExercises: List<Exercise>,
) : BaseAdapter<WorkoutPlan, LayoutItemAchievementDetailBinding>(DIFF_WORKOUT_PLAN) {

    companion object {
        val DIFF_WORKOUT_PLAN = object : DiffUtil.ItemCallback<WorkoutPlan>() {
            override fun areItemsTheSame(oldItem: WorkoutPlan, newItem: WorkoutPlan): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: WorkoutPlan, newItem: WorkoutPlan): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun createBinding(parent: ViewGroup): LayoutItemAchievementDetailBinding {
        val inflater = LayoutInflater.from(parent.context)
        return LayoutItemAchievementDetailBinding.inflate(inflater, parent, false)
    }

    override fun bind(binding: LayoutItemAchievementDetailBinding, item: WorkoutPlan, position: Int) {

        val exercise = mExercises.find { it.id == item.exercise_id }
        val category = mCategories.find { it.id == exercise?.category_id }

        if (item.progress == 100){
            binding.ivStatus.setImageResource(R.drawable.check_circle_green)
        } else {
            binding.ivStatus.setImageResource(R.drawable.check_circle_gray)
        }

        binding.imgExercisePlan.loadImage(exercise?.image_url)
        binding.tvTitleExercisePlan.text = exercise?.title
        binding.tvCategoryExercisePlan.text = category?.name
        binding.seekBar.progress = item.progress ?: 0
    }
}