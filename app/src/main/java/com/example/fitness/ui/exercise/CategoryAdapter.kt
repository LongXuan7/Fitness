package com.example.fitness.ui.exercise

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.fitness.data.model.Category
import com.example.fitness.databinding.LayoutItemCategoryBinding
import com.example.fitness.util.base.BaseAdapter
import com.example.fitness.util.ext.loadImage

class CategoryAdapter(
    private val onClick: (Category) -> Unit = {}
) : BaseAdapter<Category, LayoutItemCategoryBinding>(DIFF_EXERCISE) {

    private var fullList: List<Category> = emptyList()

    companion object {
        val DIFF_EXERCISE = object : DiffUtil.ItemCallback<Category>() {
            override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun createBinding(parent: ViewGroup): LayoutItemCategoryBinding {
        val inflater = LayoutInflater.from(parent.context)
        return LayoutItemCategoryBinding.inflate(inflater, parent, false)
    }

    override fun bind(binding: LayoutItemCategoryBinding, item: Category, position: Int) {
        binding.tvNameExercise.text = item.name
        binding.ivEx.loadImage(item.url)
        binding.root.setOnClickListener { onClick.invoke(item) }
    }

    override fun submitList(list: List<Category>?) {
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