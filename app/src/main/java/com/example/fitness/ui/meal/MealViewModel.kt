package com.example.fitness.ui.meal

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fitness.data.model.Meal
import com.example.fitness.data.model.MealCategory
import com.example.fitness.data.repository.MealCategoryRepository
import com.example.fitness.data.repository.MealRepository
import com.example.fitness.util.base.BaseViewModel
import java.util.Locale

class MealViewModel(sharedPref: SharedPreferences) : BaseViewModel() {
    val currentLanguage = sharedPref.getString("language", "vi") ?: "vi"
    private val mealCategoryRepository = MealCategoryRepository(if (currentLanguage == "en") "meal_categories_en" else "meal_categories")
    private val mealRepository = MealRepository(if (currentLanguage == "en") "meals_en" else "meals")

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