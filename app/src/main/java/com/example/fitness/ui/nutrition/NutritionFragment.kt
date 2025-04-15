package com.example.fitness.ui.nutrition

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.example.fitness.data.model.Meal
import com.example.fitness.data.model.MealItem
import com.example.fitness.data.model.MealPlan
import com.example.fitness.data.model.MyNutrition
import com.example.fitness.data.model.Sport
import com.example.fitness.databinding.FragmentNutritionBinding
import com.example.fitness.util.base.BaseFragment
import com.example.fitness.util.ext.setAdapterLinearVertical

class NutritionFragment : BaseFragment<FragmentNutritionBinding, NutritionViewModel>() {

    private var adapter: NutritionAdapter? = null
    private var myNutrition = listOf<MyNutrition>()
    private var myMeal = listOf<Meal>()
    private var mySport = listOf<Sport>()
    private var mealPlan: String? = null
    private var date: String? = null

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNutritionBinding {
        return FragmentNutritionBinding.inflate(inflater, container, false)
    }

    override fun setUpViews() {
         mealPlan = arguments?.getString("mealPlan")
         date = arguments?.getString("date")
    }

    override fun setupObservers() {
        viewModel.myNutritionList.observe(viewLifecycleOwner) {
            myNutrition = it
            trySetupAdapter()
        }
        viewModel.myMealList.observe(viewLifecycleOwner) {
            myMeal = it
            trySetupAdapter()
        }
        viewModel.mySportList.observe(viewLifecycleOwner) {
            mySport = it
            trySetupAdapter()
        }
        viewModel.deleteNutrition.observe(viewLifecycleOwner) {
            if (it) {
                adapter?.submitList(myNutrition)
            }
        }
        viewModel.addMealPlan.observe(viewLifecycleOwner) {
            if (it) {
              Toast.makeText(requireContext(), "Thêm thành công", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun trySetupAdapter() {
        if (myNutrition.isNotEmpty() && myMeal.isNotEmpty() && mySport.isNotEmpty()) {
            adapter = NutritionAdapter(
                mMeals = myMeal,
                mSport = mySport,
                onClick = ::onItemClickDelete,
                onClickAdd = ::onItemClickAdd
            )
            adapter?.let { binding.rcvNutrition.setAdapterLinearVertical(it) }
            if (date != null && mealPlan != null) {
                adapter?.showAddButton(true)
            } else {
                adapter?.showAddButton(false)
            }
            adapter?.submitList(myNutrition)
        }
    }

    private fun onItemClickAdd(myNutrition: MyNutrition) {
        date?.let { selectedDate ->
            val mealItemMap = mapOf(myNutrition.id.toString() to MealItem(nutrition_id = myNutrition.id))

            val mealPlanObject = when (mealPlan) {
                "breakfast" -> MealPlan(breakfast = mealItemMap)
                "lunch" -> MealPlan(lunch = mealItemMap)
                "dinner" -> MealPlan(dinner = mealItemMap)
                else -> null
            }

            mealPlanObject?.let {
                viewModel.addMealPlan(
                    mealPlan = it,
                    date = selectedDate
                )
            }
        }
    }


    private fun onItemClickDelete(nutrition: MyNutrition) {
        viewModel.deleteNutrition(nutrition)
    }

    override fun setUpOnClick() {}

}