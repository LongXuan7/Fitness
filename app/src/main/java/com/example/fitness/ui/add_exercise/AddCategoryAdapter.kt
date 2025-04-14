package com.example.fitness.ui.add_exercise

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.fitness.data.model.Category
import com.example.fitness.databinding.LayoutItemAddCategoryBinding
import com.example.fitness.databinding.LayoutItemCategoryBinding
import com.example.fitness.util.base.BaseAdapter
import com.example.fitness.util.ext.loadImage

class AddCategoryAdapter(
    private val onClick: (Category) -> Unit = {}
) : BaseAdapter<Category, LayoutItemAddCategoryBinding>(DIFF_EXERCISE) {

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

    override fun createBinding(parent: ViewGroup): LayoutItemAddCategoryBinding {
        val inflater = LayoutInflater.from(parent.context)
        return LayoutItemAddCategoryBinding.inflate(inflater, parent, false)
    }

    override fun bind(binding: LayoutItemAddCategoryBinding, item: Category, position: Int) {
        binding.tvNameExercise.text = item.name
        binding.tvCount.text = item.count_temp.toString()
        binding.ivEx.loadImage(item.url)
        binding.ivNextExercise.setOnClickListener { onClick.invoke(item) }
    }
}