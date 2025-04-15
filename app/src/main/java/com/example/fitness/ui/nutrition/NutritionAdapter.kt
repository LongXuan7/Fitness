package com.example.fitness.ui.nutrition

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.fitness.R
import com.example.fitness.data.model.Meal
import com.example.fitness.data.model.MyNutrition
import com.example.fitness.data.model.Sport
import com.example.fitness.databinding.LayoutItemNutritionBinding
import com.example.fitness.util.base.BaseAdapter
import com.example.fitness.util.ext.hide
import com.example.fitness.util.ext.loadImage
import com.example.fitness.util.ext.show

class NutritionAdapter(
    private val mSport: List<Sport>,
    private val mMeals: List<Meal>,
    private val onClickAdd: (MyNutrition) -> Unit = {},
    private val onClick: (MyNutrition) -> Unit = {}
) : BaseAdapter<MyNutrition, LayoutItemNutritionBinding>(DIFF_MEAL) {

    private var isShow = true

    companion object {
        val DIFF_MEAL = object : DiffUtil.ItemCallback<MyNutrition>() {
            override fun areItemsTheSame(oldItem: MyNutrition, newItem: MyNutrition): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: MyNutrition, newItem: MyNutrition): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun createBinding(parent: ViewGroup): LayoutItemNutritionBinding {
        val inflater = LayoutInflater.from(parent.context)
        return LayoutItemNutritionBinding.inflate(inflater, parent, false)
    }

    override fun bind(binding: LayoutItemNutritionBinding, item: MyNutrition, position: Int) {
        val meal = mMeals.find { it.id == item.meal_id }
        val sport = mSport.find { it.id == item.sport_id }

        if (meal != null) {
            binding.ivNutrition.loadImage(meal.image)
            binding.tvTitleNutrition.text = meal.title
            binding.tvKcalNutrition.text = meal.calo
            binding.tvProteinNutrition.text =
                binding.root.context.getString(R.string.protein, meal.protein)
            binding.imageView9.show()
            binding.tvKcalNutrition.show()
            binding.tvProteinNutrition.show()
            binding.imageView10.show()
        } else {
            binding.ivNutrition.loadImage(sport?.image)
            binding.tvTitleNutrition.text = sport?.title
            binding.imageView9.hide()
            binding.tvKcalNutrition.hide()
            binding.tvProteinNutrition.hide()
            binding.imageView10.hide()
        }
        if (isShow) binding.btnAdd.show() else binding.btnAdd.hide()
        binding.btnAdd.setOnClickListener { onClickAdd.invoke(item) }
        binding.root.setOnClickListener { onClick.invoke(item) }
    }

    fun showAddButton(boolean: Boolean) {
        isShow = boolean
    }
}