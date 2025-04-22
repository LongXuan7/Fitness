package com.example.fitness.ui.sport

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fitness.data.model.Sport
import com.example.fitness.data.model.SportCategory
import com.example.fitness.data.repository.SportCategoryRepository
import com.example.fitness.data.repository.SportRepository
import com.example.fitness.util.base.BaseViewModel
import java.util.Locale

class SportViewModel(sharedPref: SharedPreferences) : BaseViewModel() {
    val currentLanguage = sharedPref.getString("language", "vi") ?: "vi"
    private val sportCategoryRepository = SportCategoryRepository(if (currentLanguage == "en") "sport_categories_en" else "sport_categories")
    private val sportRepository = SportRepository(if (currentLanguage == "en") "sports_en" else "sports")

    init {
        getSportCategories()
    }

    private val _sportCategories = MutableLiveData<List<SportCategory>>()
    val sportCategories: LiveData<List<SportCategory>>
        get() = _sportCategories

    private fun getSportCategories() {
        launchWithErrorHandling(
            block = {
                sportCategoryRepository.getAll(
                    onResult = { list ->
                        _sportCategories.postValue(list)
                    },
                    onError = { message ->
                        throw Exception(message)
                    }
                )
            }
        )
    }

    private val _sport = MutableLiveData<List<Sport>>()
    val sport: LiveData<List<Sport>>
        get() = _sport

    fun getMeals(id : String?) {
        launchWithErrorHandling(
            block = {
                sportRepository.getAll(
                    onResult = { list ->
                        val listFilter = list.filter { it.sportCategoryId == id}
                        _sport.postValue(listFilter)
                    },
                    onError = { message ->
                        throw Exception(message)
                    }
                )
            }
        )
    }
}