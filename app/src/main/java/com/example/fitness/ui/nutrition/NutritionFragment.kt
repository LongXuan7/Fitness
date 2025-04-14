package com.example.fitness.ui.nutrition

import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.fitness.data.model.Meal
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

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentNutritionBinding {
        return FragmentNutritionBinding.inflate(inflater, container, false)
    }

    override fun setUpViews() {
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
    }

    private fun trySetupAdapter() {
        if (myNutrition.isNotEmpty() && myMeal.isNotEmpty() && mySport.isNotEmpty()) {
            adapter = NutritionAdapter(
                mMeals = myMeal,
                mSport = mySport,
                onClick = ::onItemClickDelete
            )
            adapter?.let { binding.rcvNutrition.setAdapterLinearVertical(it) }
            adapter?.submitList(myNutrition)
        }
    }

    private fun onItemClickDelete(myNutrition: MyNutrition) {

    }

    override fun setUpOnClick() {}

}