package com.example.fitness.ui.sport

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.fitness.data.model.Meal
import com.example.fitness.data.model.MealCategory
import com.example.fitness.data.model.Sport
import com.example.fitness.databinding.LayoutItemBenefitBinding
import com.example.fitness.databinding.LayoutItemMealBinding
import com.example.fitness.databinding.LayoutItemMealCategoryBinding
import com.example.fitness.util.base.BaseAdapter
import com.example.fitness.util.ext.loadImage


class BenefitAdapter: BaseAdapter<Sport.Benefit, LayoutItemBenefitBinding>(DIFF_BENEFIT) {

    companion object {
        val DIFF_BENEFIT = object : DiffUtil.ItemCallback<Sport.Benefit>() {
            override fun areItemsTheSame(oldItem: Sport.Benefit, newItem: Sport.Benefit): Boolean {
                return oldItem.description == newItem.description
            }

            override fun areContentsTheSame(oldItem: Sport.Benefit, newItem: Sport.Benefit): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun createBinding(parent: ViewGroup): LayoutItemBenefitBinding {
        val inflater = LayoutInflater.from(parent.context)
        return LayoutItemBenefitBinding.inflate(inflater, parent, false)
    }

    override fun bind(binding: LayoutItemBenefitBinding, item: Sport.Benefit, position: Int) {
        binding.tvDescriptionBenefit.text = item.description
    }
}