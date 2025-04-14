package com.example.fitness.ui.my_meal_plan

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.fitness.R
import com.example.fitness.databinding.ActivityMyMealPlanBinding
import com.example.fitness.ui.food.FoodFragment
import com.example.fitness.ui.main.MainActivity
import com.example.fitness.ui.meal_plan.MealPlanFragment
import com.example.fitness.ui.nutrition.NutritionFragment
import com.example.fitness.util.OnMainFragmentListener
import com.example.fitness.util.base.BaseActivity

class MyMealPlanActivity : BaseActivity<ActivityMyMealPlanBinding, MyMealPlanViewModel>() {

    override val bindingInflater: (LayoutInflater) -> ActivityMyMealPlanBinding
        get() = ActivityMyMealPlanBinding::inflate

    override fun setupViews() {
        binding.headerTitle.tvTitle.text = getString(R.string.my_meal_plan)
        setTabSelected(true)
        transitionToFragment(NutritionFragment())
    }

    override fun setupOnClick() {

        binding.headerTitle.ivBack.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("FoodFragment", true)
            startActivity(intent)
        }

        binding.tvNutrition.apply {
            binding.tvNutrition.setOnClickListener {
                setTabSelected(true)
                transitionToFragment(NutritionFragment())
            }

            binding.tvMealPlan.setOnClickListener {
                setTabSelected(false)
                transitionToFragment(MealPlanFragment())
            }
        }
    }

    private fun setTabSelected(isNutritionSelected: Boolean) {
        if (isNutritionSelected) {
            setActive(binding.tvNutrition)
            setInactive(binding.tvMealPlan)
        } else {
            setInactive(binding.tvNutrition)
            setActive(binding.tvMealPlan)
        }
    }

    private fun setActive(view: android.widget.TextView) {
        view.background = ContextCompat.getDrawable(this, R.drawable.bg_btn_meal_plan)
        view.setTextColor(ContextCompat.getColor(this, R.color.white))
    }

    private fun setInactive(view: android.widget.TextView) {
        view.background = ContextCompat.getDrawable(this, R.drawable.bg_btn_meal_plan_gray)
        view.setTextColor(ContextCompat.getColor(this, R.color.black))
    }


    override fun setupObservers() {

    }

    private fun transitionToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.frameLayout_meal_plan, fragment)
            .addToBackStack(null)
            .commit()
    }
}