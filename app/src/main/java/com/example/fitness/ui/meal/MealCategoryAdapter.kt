package com.example.fitness.ui.meal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.fitness.data.model.Meal
import com.example.fitness.data.model.MealCategory
import com.example.fitness.databinding.LayoutItemMealCategoryBinding
import com.example.fitness.util.base.BaseAdapter
import com.example.fitness.util.ext.loadImage


class MealCategoryAdapter(
    private val onClick: (MealCategory) -> Unit = {}
) : BaseAdapter<MealCategory, LayoutItemMealCategoryBinding>(DIFF_MEAL_CATEGORY) {

    private var fullList: List<MealCategory> = emptyList()

    companion object {
        val DIFF_MEAL_CATEGORY = object : DiffUtil.ItemCallback<MealCategory>() {
            override fun areItemsTheSame(oldItem: MealCategory, newItem: MealCategory): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MealCategory, newItem: MealCategory): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun createBinding(parent: ViewGroup): LayoutItemMealCategoryBinding {
        val inflater = LayoutInflater.from(parent.context)
        return LayoutItemMealCategoryBinding.inflate(inflater, parent, false)
    }

    override fun bind(binding: LayoutItemMealCategoryBinding, item: MealCategory, position: Int) {
        binding.tvTitleMealCategory.text = item.title
        binding.ivMealCategory.loadImage(item.image)
        binding.root.setOnClickListener { onClick.invoke(item) }
    }

    override fun submitList(list: List<MealCategory>?) {
        if (list != null) {
            fullList = list
        }
        super.submitList(list)
    }


    fun filter(query: String) {
        val filteredList = if (query.isBlank()) {
            fullList
        } else {
            fullList.filter {
                it.title?.contains(query, ignoreCase = true) == true
            }
        }
        super.submitList(filteredList)
    }
}