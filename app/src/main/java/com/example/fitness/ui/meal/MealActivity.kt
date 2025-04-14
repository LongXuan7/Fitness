package com.example.fitness.ui.meal

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import com.example.fitness.R
import com.example.fitness.data.model.Meal
import com.example.fitness.data.model.MealCategory
import com.example.fitness.databinding.ActivityMealBinding
import com.example.fitness.ui.meal_detail.MealDetailActivity
import com.example.fitness.util.base.BaseActivity
import com.example.fitness.util.ext.setAdapterLinearVertical

class MealActivity : BaseActivity<ActivityMealBinding, MealViewModel>() {

    private val adapterMealCategory = MealCategoryAdapter(::onItemClickMealCategory)
    private val adapterMeal = MealAdapter(::onItemClickMeal)
    private var isBack = false

    override val bindingInflater: (LayoutInflater) -> ActivityMealBinding
        get() = ActivityMealBinding::inflate

    override fun setupViews() {
        binding.headerTitle.tvTitle.text = getString(R.string.meal_category)
        setUpRecyclerViewMealCategory()
    }

    private fun setUpRecyclerViewMealCategory() {
        binding.rcvMealCategory.setAdapterLinearVertical(adapterMealCategory)
    }

    private fun setUpRecyclerViewMeal() {
        binding.rcvMealCategory.setAdapterLinearVertical(adapterMeal)
    }

    override fun setupOnClick() {
        binding.headerTitle.ivBack.setOnClickListener {
            if (isBack) {
                setUpRecyclerViewMealCategory()
                binding.etSearch.text?.clear()
                isBack = false
            } else {
                onBackPressedDispatcher.onBackPressed()
            }
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                if (isBack) {
                    adapterMeal.filter(query)
                } else {
                    adapterMealCategory.filter(query)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

    }

    override fun setupObservers() {
        viewModel.mealCategories.observe(this) {
            adapterMealCategory.submitList(it)
        }
        viewModel.meals.observe(this) {
            adapterMeal.submitList(it)
        }
    }

    private fun onItemClickMealCategory(mealCategory: MealCategory) {
        setUpRecyclerViewMeal()
        viewModel.getMeals(mealCategory.id)
        isBack = true
    }

    private fun onItemClickMeal(meal: Meal) {
        startActivity(Intent(this, MealDetailActivity::class.java).apply {
            putExtra("meal", meal)
        })
    }
}