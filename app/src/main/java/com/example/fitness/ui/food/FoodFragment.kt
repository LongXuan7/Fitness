package com.example.fitness.ui.food

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.fitness.databinding.FragmentFoodBinding
import com.example.fitness.ui.meal.MealActivity
import com.example.fitness.ui.my_meal_plan.MyMealPlanActivity
import com.example.fitness.ui.sport.SportActivity
import com.example.fitness.util.base.BaseFragment
import org.koin.androidx.viewmodel.ext.android.viewModel

class FoodFragment : BaseFragment<FragmentFoodBinding, FoodViewModel>() {
    override val viewModel: FoodViewModel by viewModel()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentFoodBinding {
        return FragmentFoodBinding.inflate(inflater, container, false)
    }

    override fun setUpViews() {

    }

    override fun setupObservers() {

    }

    override fun setUpOnClick() {
        binding.tvMy.setOnClickListener { startActivity(Intent(requireContext(), MyMealPlanActivity::class.java)) }
        binding.tvMeal.setOnClickListener { startActivity(Intent(requireContext(), MealActivity::class.java)) }
        binding.tvSport.setOnClickListener { startActivity(Intent(requireContext(), SportActivity::class.java)) }
    }
}