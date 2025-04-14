package com.example.fitness.ui.nutrition

import androidx.lifecycle.MutableLiveData
import com.example.fitness.data.model.Meal
import com.example.fitness.data.model.MyNutrition
import com.example.fitness.data.model.Sport
import com.example.fitness.data.repository.MealRepository
import com.example.fitness.data.repository.MyNutritionRepository
import com.example.fitness.data.repository.SportRepository
import com.example.fitness.util.base.BaseViewModel

class NutritionViewModel : BaseViewModel() {
    private val repositoryNutrition = MyNutritionRepository("my_nutritions")
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

}