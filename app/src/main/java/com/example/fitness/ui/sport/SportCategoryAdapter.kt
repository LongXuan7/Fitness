package com.example.fitness.ui.sport

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.fitness.data.model.Meal
import com.example.fitness.data.model.MealCategory
import com.example.fitness.data.model.SportCategory
import com.example.fitness.databinding.LayoutItemMealCategoryBinding
import com.example.fitness.databinding.LayoutItemSportCategoryBinding
import com.example.fitness.util.base.BaseAdapter
import com.example.fitness.util.ext.loadImage


class SportCategoryAdapter(
    private val onClick: (SportCategory) -> Unit = {}
) : BaseAdapter<SportCategory, LayoutItemSportCategoryBinding>(DIFF_SPORT_CATEGORY) {

    private var fullList: List<SportCategory> = emptyList()

    companion object {
        val DIFF_SPORT_CATEGORY = object : DiffUtil.ItemCallback<SportCategory>() {
            override fun areItemsTheSame(oldItem: SportCategory, newItem: SportCategory): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: SportCategory, newItem: SportCategory): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun createBinding(parent: ViewGroup): LayoutItemSportCategoryBinding {
        val inflater = LayoutInflater.from(parent.context)
        return LayoutItemSportCategoryBinding.inflate(inflater, parent, false)
    }

    override fun bind(binding: LayoutItemSportCategoryBinding, item: SportCategory, position: Int) {
        binding.tvNameCategory.text = item.name
        binding.ivSportCategory.loadImage("https://res.cloudinary.com/dgdb5znxn/image/upload/v1744527604/fitness/hwtd1jx1oum09chtipq0.png")
        binding.tvNextCategorySport.setOnClickListener { onClick.invoke(item) }
    }

    override fun submitList(list: List<SportCategory>?) {
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
                it.name?.contains(query, ignoreCase = true) == true
            }
        }
        super.submitList(filteredList)
    }
}