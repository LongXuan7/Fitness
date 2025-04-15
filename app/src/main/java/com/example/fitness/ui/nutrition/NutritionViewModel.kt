package com.example.fitness.ui.nutrition

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
import java.time.LocalDate

class NutritionViewModel : BaseViewModel() {
    private val repositoryNutrition = MyNutritionRepository("my_nutritions")
    private val mealPlanRepository = MealPlanRepository("meal_plans")
    private val repositoryMeal = MealRepository("meals")
    private val repositorySport= SportRepository("sports")

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
                repositoryNutrition.getAll(
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

    private val _addMealPlan = MutableLiveData(false)
    val addMealPlan: MutableLiveData<Boolean>
        get() = _addMealPlan

    fun addMealPlan(mealPlan: MealPlan, date: String) {
        launchWithErrorHandling(
            block = {
                mealPlanRepository.add(
                    date,
                    mealPlan,
                    onComplete = {_addMealPlan.postValue(true)}
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
                    onComplete = {_deleteNutrition.postValue(true)}
                )
            }
        )
    }
}