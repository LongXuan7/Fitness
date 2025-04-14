package com.example.fitness.ui.exercise

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.fitness.data.model.Category
import com.example.fitness.data.model.Exercise
import com.example.fitness.data.model.Sport
import com.example.fitness.databinding.LayoutItemCategoryBinding
import com.example.fitness.util.base.BaseAdapter
import com.example.fitness.util.ext.loadImage

class ExerciseAdapter(
    private val onClick: (Exercise) -> Unit = {}
) : BaseAdapter<Exercise, LayoutItemCategoryBinding>(DIFF_EXERCISE) {

    private var fullList: List<Exercise> = emptyList()

    companion object {
        val DIFF_EXERCISE = object : DiffUtil.ItemCallback<Exercise>() {
            override fun areItemsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Exercise, newItem: Exercise): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun createBinding(parent: ViewGroup): LayoutItemCategoryBinding {
        val inflater = LayoutInflater.from(parent.context)
        return LayoutItemCategoryBinding.inflate(inflater, parent, false)
    }

    override fun bind(binding: LayoutItemCategoryBinding, item: Exercise, position: Int) {
        binding.tvNameExercise.text = item.title
        binding.tvNameExercise.textSize = 16f
        binding.ivEx.loadImage(item.image_url)
        binding.root.setOnClickListener { onClick.invoke(item) }
    }

    override fun submitList(list: List<Exercise>?) {
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
                it.title.contains(query, ignoreCase = true)
            }
        }
        super.submitList(filteredList)
    }
}