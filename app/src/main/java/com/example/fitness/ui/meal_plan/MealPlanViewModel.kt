package com.example.fitness.ui.meal_plan

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
import java.time.LocalDate

class MealPlanViewModel : BaseViewModel() {

    private val repository = MealPlanRepository("meal_plans")
    private val repositoryNutrition = MyNutritionRepository("my_nutritions")
    private val repositoryMeal = MealRepository("meals")
    private val repositorySport= SportRepository("sports")

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
}