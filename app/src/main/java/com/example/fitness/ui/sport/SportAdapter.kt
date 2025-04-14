package com.example.fitness.ui.sport

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.fitness.data.model.Meal
import com.example.fitness.data.model.MealCategory
import com.example.fitness.data.model.Sport
import com.example.fitness.databinding.LayoutItemMealBinding
import com.example.fitness.databinding.LayoutItemMealCategoryBinding
import com.example.fitness.databinding.LayoutItemSportCategoryBinding
import com.example.fitness.util.base.BaseAdapter
import com.example.fitness.util.ext.loadImage


class SportAdapter(
    private val onClick: (Sport) -> Unit = {}
) : BaseAdapter<Sport, LayoutItemSportCategoryBinding>(DIFF_SPORT) {

    private var fullList: List<Sport> = emptyList()

    companion object {
        val DIFF_SPORT = object : DiffUtil.ItemCallback<Sport>() {
            override fun areItemsTheSame(oldItem: Sport, newItem: Sport): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Sport, newItem: Sport): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun createBinding(parent: ViewGroup): LayoutItemSportCategoryBinding {
        val inflater = LayoutInflater.from(parent.context)
        return LayoutItemSportCategoryBinding.inflate(inflater, parent, false)
    }

    override fun bind(binding: LayoutItemSportCategoryBinding, item: Sport, position: Int) {
        binding.tvNameCategory.text = item.title
        binding.ivSportCategory.loadImage(item.thumnal)
        val layoutParams = binding.ivSportCategory.layoutParams
        val scale = binding.root.context.resources.displayMetrics.density
        val sizeInPx = (120 * scale + 0.5f).toInt()
        val paddingInPx = (30 * scale + 0.5f).toInt()
        layoutParams.width = sizeInPx
        layoutParams.height = sizeInPx
        binding.ivSportCategory.layoutParams = layoutParams
        binding.ivSportCategory.setPadding(paddingInPx, binding.ivSportCategory.paddingTop, binding.ivSportCategory.paddingRight, binding.ivSportCategory.paddingBottom)
        binding.tvNextCategorySport.setOnClickListener { onClick.invoke(item) }
    }

    override fun submitList(list: List<Sport>?) {
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