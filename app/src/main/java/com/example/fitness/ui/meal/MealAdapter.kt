package com.example.fitness.ui.meal

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.fitness.data.model.Meal
import com.example.fitness.data.model.MealCategory
import com.example.fitness.databinding.LayoutItemMealBinding
import com.example.fitness.databinding.LayoutItemMealCategoryBinding
import com.example.fitness.util.base.BaseAdapter
import com.example.fitness.util.ext.loadImage


class MealAdapter(
    private val onClick: (Meal) -> Unit = {}
) : BaseAdapter<Meal, LayoutItemMealBinding>(DIFF_MEAL) {

    private var fullList: List<Meal> = emptyList()

    companion object {
        val DIFF_MEAL = object : DiffUtil.ItemCallback<Meal>() {
            override fun areItemsTheSame(oldItem: Meal, newItem: Meal): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Meal, newItem: Meal): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun createBinding(parent: ViewGroup): LayoutItemMealBinding {
        val inflater = LayoutInflater.from(parent.context)
        return LayoutItemMealBinding.inflate(inflater, parent, false)
    }

    override fun bind(binding: LayoutItemMealBinding, item: Meal, position: Int) {
        binding.tvTitle.text = item.title
        binding.tvKcal.text = item.calo
        binding.root.setOnClickListener { onClick.invoke(item) }
    }

    override fun submitList(list: List<Meal>?) {
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