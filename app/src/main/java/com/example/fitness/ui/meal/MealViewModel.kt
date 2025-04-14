package com.example.fitness.ui.meal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fitness.data.model.Meal
import com.example.fitness.data.model.MealCategory
import com.example.fitness.data.repository.MealCategoryRepository
import com.example.fitness.data.repository.MealRepository
import com.example.fitness.util.base.BaseViewModel

class MealViewModel : BaseViewModel() {
    private val mealCategoryRepository = MealCategoryRepository("meal_categories")
    private val mealRepository = MealRepository("meals")

    init {
        getMealCategories()
    }


    private val _mealCategories = MutableLiveData<List<MealCategory>>()
    val mealCategories: LiveData<List<MealCategory>>
        get() = _mealCategories

    private fun getMealCategories() {
       launchWithErrorHandling(
           block = {
               mealCategoryRepository.getAll(
                   onResult = { list ->
                       _mealCategories.postValue(list)
                   },
                   onError = { message ->
                       throw Exception(message)
                   }
               )
           }
       )
    }

    private val _meals = MutableLiveData<List<Meal>>()
    val meals: LiveData<List<Meal>>
        get() = _meals

    fun getMeals(id : String?) {
        launchWithErrorHandling(
            block = {
                mealRepository.getAll(
                    onResult = { list ->
                        list.filter { it.id == id}
                        _meals.postValue(list)
                    },
                    onError = { message ->
                        throw Exception(message)
                    }
                )
            }
        )
    }
}