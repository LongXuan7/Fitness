package com.example.fitness.ui.add_exercise

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import com.example.fitness.R
import com.example.fitness.data.model.Category
import com.example.fitness.data.model.Exercise
import com.example.fitness.databinding.LayoutItemAddExerciseBinding
import com.example.fitness.databinding.LayoutItemCategoryBinding
import com.example.fitness.util.base.BaseAdapter
import com.example.fitness.util.ext.loadImage

class AddExerciseAdapter(
    private val onClick: (Exercise) -> Unit = {},
    private val onclickItemCount: (List<Exercise>) -> Unit = {},
    private val onClickUpdate : (Exercise) -> Unit = {}
) : BaseAdapter<Exercise, LayoutItemAddExerciseBinding>(DIFF_EXERCISE) {

    private val selectedExercises = mutableListOf<Exercise>()

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

    override fun createBinding(parent: ViewGroup): LayoutItemAddExerciseBinding {
        val inflater = LayoutInflater.from(parent.context)
        return LayoutItemAddExerciseBinding.inflate(inflater, parent, false)
    }

    @SuppressLint("SetTextI18n")
    override fun bind(binding: LayoutItemAddExerciseBinding, item: Exercise, position: Int) {
        binding.tvNameExercise.text = item.title
        binding.ivEx.loadImage(item.image_url)
        binding.tvCount.text = item.set_temp.toString()

        binding.btnAdd.setOnClickListener {
            if (binding.tvCount.text == "10") {
                Toast.makeText(
                    binding.root.context,
                    "You can't add more than 10 exercises",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                binding.tvCount.text = (binding.tvCount.text.toString().toInt() + 1).toString()
                reload(binding, item)
            }
        }

        binding.btnRemove.setOnClickListener {
            if (binding.tvCount.text == "0") {
                Toast.makeText(
                    binding.root.context,
                    "You can't remove more than 0 exercises",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                binding.tvCount.text = (binding.tvCount.text.toString().toInt() - 1).toString()
                reload(binding, item)
            }
        }

        binding.root.setOnClickListener {
            onClick.invoke(item)
        }

        reload(binding, item)
    }

    private fun reload(binding: LayoutItemAddExerciseBinding, item: Exercise) {
        val count = binding.tvCount.text.toString().toInt()

        if (count == 0) {
            binding.ivStatus.setImageResource(R.drawable.check_circle_gray)
            onClickUpdate.invoke(item)
            selectedExercises.removeAll { it.id == item.id }
        } else {
            binding.ivStatus.setImageResource(R.drawable.check_circle_green)

            val existingItem = selectedExercises.find { it.id == item.id }

            if (existingItem != null) {
                existingItem.set_temp = count
            } else {
                selectedExercises.add(item.copy(set_temp = count))
            }
        }

        onclickItemCount.invoke(selectedExercises)
    }
}