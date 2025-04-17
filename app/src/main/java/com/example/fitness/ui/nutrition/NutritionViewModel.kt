package com.example.fitness.ui.nutrition

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import com.example.fitness.data.model.Meal
import com.example.fitness.data.model.MealPlan
import com.example.fitness.data.model.MyNutrition
import com.example.fitness.data.model.Sport
import com.example.fitness.data.repository.MealPlanRepository
import com.example.fitness.data.repository.MealRepository
import com.example.fitness.data.repository.MyNutritionRepository
import com.example.fitness.data.repository.SportRepository
import com.example.fitness.util.base.BaseViewModel
import java.util.Locale

class NutritionViewModel(sharedPref: SharedPreferences) : BaseViewModel() {
    val currentLanguage = sharedPref.getString("language", "vi") ?: "vi"
    private val repositoryNutrition = MyNutritionRepository("my_nutritions")
    private val repositoryNutritionEn = MyNutritionRepository("my_nutritions_en")
    private val mealPlanRepository = MealPlanRepository("meal_plans")
    private val mealPlanRepositoryEn = MealPlanRepository("meal_plans_en")
    private val repositoryMeal = MealRepository("meals")
    private val repositoryMealEn = MealRepository("meals_en")
    private val repositorySport = SportRepository("sports")
    private val repositorySportEn = SportRepository("sports_en")

    init {
        getMyNutritionList()
        getMyMealList()
        getMySportList()
    }

    private val _myNutritionList = MutableLiveData<List<MyNutrition>>()
    val myNutritionList: MutableLiveData<List<MyNutrition>>
        get() = _myNutritionList

    private fun getMyNutritionList() {
        launchWithErrorHandling(
            block = {
                if (currentLanguage == "en") {
                    repositoryNutritionEn
                } else {
                    repositoryNutrition
                }.getAll(
                    onResult = { list ->
                        _myNutritionList.postValue(list)
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
                if (currentLanguage == "en") {
                    repositoryMealEn
                } else {
                    repositoryMeal
                }.getAll(
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
                if (currentLanguage == "en") {
                    repositorySportEn
                } else {
                    repositorySport
                }.getAll(
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

    private val _addMealPlan = MutableLiveData(false)
    val addMealPlan: MutableLiveData<Boolean>
        get() = _addMealPlan

    fun addMealPlan(mealPlan: MealPlan, date: String) {
        launchWithErrorHandling(
            block = {
                mealPlanRepository.add(
                    date,
                    mealPlan,
                    onComplete = {
                        if (currentLanguage == "vi") {
                            _addMealPlan.postValue(true)
                        }
                    }
                )
                mealPlanRepositoryEn.add(
                    date,
                    mealPlan,
                    onComplete = {
                        if (currentLanguage == "en") {
                            _addMealPlan.postValue(true)
                        }
                    }
                )
            }
        )
    }

    private val _deleteNutrition = MutableLiveData(false)
    val deleteNutrition: MutableLiveData<Boolean>
        get() = _deleteNutrition

    fun deleteNutrition(nutrition: MyNutrition) {
        launchWithErrorHandling(
            block = {
                repositoryNutrition.delete(
                    nutrition.id.toString(),
                    onComplete = {  if (currentLanguage == "vi") {
                        _addMealPlan.postValue(true)
                    } }
                )
                repositoryNutritionEn.delete(
                    nutrition.id.toString(),
                    onComplete = {  if (currentLanguage == "en") {
                        _addMealPlan.postValue(true)
                    } }
                )
            }
        )
    }
}