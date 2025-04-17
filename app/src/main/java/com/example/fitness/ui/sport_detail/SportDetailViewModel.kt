package com.example.fitness.ui.sport_detail

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fitness.data.model.MyNutrition
import com.example.fitness.data.repository.MyNutritionRepository
import com.example.fitness.util.base.BaseViewModel
import java.util.Locale

class SportDetailViewModel(sharedPref: SharedPreferences) : BaseViewModel() {
    val currentLanguage = sharedPref.getString("language", "vi") ?: "vi"
    private val myNutritionRepository = MyNutritionRepository("my_nutritions")
    private val myNutritionRepositoryEn = MyNutritionRepository("my_nutritions_en")

    private val _addMyNutrition = MutableLiveData(false)
    val addMyNutrition : LiveData<Boolean> get() = _addMyNutrition

    fun addMyNutrition(myNutrition: MyNutrition) {
        launchWithErrorHandling(
            block = {
                myNutritionRepository.add(
                    myNutrition.id.toString(),
                    data = myNutrition,
                    onComplete = {
                       if (currentLanguage == "vi") {
                           _addMyNutrition.postValue(it)
                       }
                    }
                )
                myNutritionRepositoryEn.add(
                    myNutrition.id.toString(),
                    data = myNutrition,
                    onComplete = {
                        if (currentLanguage == "en") {
                            _addMyNutrition.postValue(it)
                        }
                    }
                )
            }
        )
    }
}