package com.example.fitness.ui.meal_plan

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fitness.data.model.Meal
import com.example.fitness.data.model.MealItem
import com.example.fitness.data.model.MyNutrition
import com.example.fitness.data.model.Sport
import com.example.fitness.data.repository.MealPlanRepository
import com.example.fitness.data.repository.MealRepository
import com.example.fitness.data.repository.MyNutritionRepository
import com.example.fitness.data.repository.SportRepository
import com.example.fitness.util.base.BaseViewModel
import com.google.firebase.auth.FirebaseAuth
import java.time.LocalDate
import java.util.Locale

class MealPlanViewModel(sharedPref: SharedPreferences) : BaseViewModel() {
    val currentLanguage = sharedPref.getString("language", "vi") ?: "vi"
    private val repository = MealPlanRepository(if (currentLanguage == "em") "meal_plans_en" else "meal_plans")
    private val repositoryNutrition = MyNutritionRepository(if (currentLanguage == "en") "my_nutritions_en" else "my_nutritions")
    private val repositoryMeal = MealRepository(if (currentLanguage == "en") "meals_en" else "meals")
    private val repositorySport= SportRepository(if (currentLanguage == "en") "sports_en" else "sports")

    init {
        getMyMealList()
        getMySportList()
        getMyNutritionList()
        getMealPlans(LocalDate.now().toString())
    }

    private val _mealPlanListBreakFast = MutableLiveData<List<MealItem>>()
    val mealPlanListBreakFas: LiveData<List<MealItem>> get() = _mealPlanListBreakFast

    private val _mealPlanListLunch = MutableLiveData<List<MealItem>>()
    val mealPlanListLunch: LiveData<List<MealItem>> get() = _mealPlanListLunch

    private val _mealPlanListDinner = MutableLiveData<List<MealItem>>()
    val mealPlanListDinner: LiveData<List<MealItem>> get() = _mealPlanListDinner

    fun getMealPlans(date: String) {
        launchWithErrorHandling(
            block = {
                repository.getMealPlanById(
                    id = date,
                    onResult = { breakfastList, lunchList, dinnerList ->
                        _mealPlanListBreakFast.postValue(breakfastList)
                        _mealPlanListLunch.postValue(lunchList)
                        _mealPlanListDinner.postValue(dinnerList)
                    },
                    onError = { message ->
                        throw Exception(message)
                    }
                )
            }
        )
    }

    private val _myNutritionList = MutableLiveData<List<MyNutrition>>()
    val myNutritionList: MutableLiveData<List<MyNutrition>>
        get() = _myNutritionList

    private fun getMyNutritionList() {
        launchWithErrorHandling(
            block = {
                repositoryNutrition.getAll(
                    onResult = { list ->
                        val userId = FirebaseAuth.getInstance().currentUser?.uid.toString()
                        val filteredList = list.filter { it.user_id == userId }
                        _myNutritionList.postValue(filteredList)
                    },
                    onError = { message ->
                        throw Exception(message)
                    }
                )
            }
        )
    }

    private val _myMealList = MutableLiveData<List<Meal>>()
    val myMealList: MutableLiveData<List<Meal>>
        get() = _myMealList

    private fun getMyMealList() {
        launchWithErrorHandling(
            block = {
                repositoryMeal.getAll(
                    onResult = { list ->
                        _myMealList.postValue(list)
                    },
                    onError = { message ->
                        throw Exception(message)
                    }
                )
            }
        )
    }

    private val _mySportList = MutableLiveData<List<Sport>>()
    val mySportList: MutableLiveData<List<Sport>>
        get() = _mySportList

    private fun getMySportList() {
        launchWithErrorHandling(
            block = {
                repositorySport.getAll(
                    onResult = { list ->
                        _mySportList.postValue(list)
                    },
                    onError = { message ->
                        throw Exception(message)
                    }
                )
            }
        )
    }

    private val _delete = MutableLiveData<Boolean>()
    val delete: MutableLiveData<Boolean>
        get() = _delete

    fun delete(date: String, mealType: String, nutritionId: String) {
        launchWithErrorHandling(
            block = {
                repository.deleteMealItem(date, mealType, nutritionId) {
                    _delete.postValue(it)
                }
            }
        )
    }
}