package com.example.fitness.ui.sport

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fitness.data.model.Sport
import com.example.fitness.data.model.SportCategory
import com.example.fitness.data.repository.SportCategoryRepository
import com.example.fitness.data.repository.SportRepository
import com.example.fitness.util.base.BaseViewModel

class SportViewModel : BaseViewModel() {
    private val sportCategoryRepository = SportCategoryRepository("sport_categories")
    private val sportRepository = SportRepository("sports")

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
                        list.filter { it.id == id}
                        _sport.postValue(list)
                    },
                    onError = { message ->
                        throw Exception(message)
                    }
                )
            }
        )
    }
}