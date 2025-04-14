package com.example.fitness.ui.meal_detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fitness.data.model.MyNutrition
import com.example.fitness.data.repository.MyNutritionRepository
import com.example.fitness.util.base.BaseViewModel

class MealDetailViewModel : BaseViewModel() {
    private val myNutritionRepository = MyNutritionRepository("my_nutritions")

    private val _addMyNutrition = MutableLiveData(false)
    val addMyNutrition : LiveData<Boolean> get() = _addMyNutrition

    fun addMyNutrition(myNutrition: MyNutrition) {
        launchWithErrorHandling(
            block = {
                myNutritionRepository.add(
                    myNutrition.id.toString(),
                    data = myNutrition,
                    onComplete = {
                        _addMyNutrition.postValue(it)
                    }
                )
            }
        )
    }
}