package com.example.fitness.ui.plan

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.fitness.R
import com.example.fitness.data.model.Category
import com.example.fitness.data.model.Exercise
import com.example.fitness.data.model.WorkoutPlan
import com.example.fitness.databinding.LayoutItemPlanBinding
import com.example.fitness.util.base.BaseAdapter
import com.example.fitness.util.ext.hide
import com.example.fitness.util.ext.loadImage
import java.time.LocalDate

class WorkoutPlanAdapter(
    private val mCategories: List<Category>,
    private val mExercises: List<Exercise>,
    private val onClick: (WorkoutPlan) -> Unit = {},
    private val onClickDelete: (WorkoutPlan) -> Unit = {}
) : BaseAdapter<WorkoutPlan, LayoutItemPlanBinding>(DIFF_WORKOUT_PLAN) {

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

    override fun createBinding(parent: ViewGroup): LayoutItemPlanBinding {
        val inflater = LayoutInflater.from(parent.context)
        return LayoutItemPlanBinding.inflate(inflater, parent, false)
    }

    override fun bind(binding: LayoutItemPlanBinding, item: WorkoutPlan, position: Int) {

        val exercise = mExercises.find { it.id == item.exercise_id }
        val category = mCategories.find { it.id == exercise?.category_id }

        binding.imgExercisePlan.loadImage(exercise?.image_url)
        binding.tvTitleExercisePlan.text = exercise?.title
        binding.tvCategoryExercisePlan.text = category?.name
        binding.tvSetExercisePlan.text = binding.root.context.getString(R.string.sets, item.set.toString())
        binding.seekBar.progress = item.progress ?: 0

        val itemDate = LocalDate.parse(item.time)
        val currentDate = LocalDate.now()

        if (!itemDate.isBefore(currentDate)) {
            binding.btnDeleteExercisePlan.setOnClickListener { onClickDelete.invoke(item) }
            binding.root.setOnClickListener { onClick.invoke(item) }
        } else {
            binding.btnDeleteExercisePlan.setOnClickListener(null)
            binding.btnDeleteExercisePlan.hide()
            binding.root.setOnClickListener(null)
        }
    }
}