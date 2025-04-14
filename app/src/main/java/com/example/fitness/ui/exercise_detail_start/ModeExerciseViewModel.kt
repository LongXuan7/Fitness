package com.example.fitness.ui.exercise_detail_start

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.fitness.data.repository.WorkoutPlanRepository
import com.example.fitness.util.base.BaseViewModel

class ModeExerciseViewModel : BaseViewModel() {

    private val workoutPlanRepository = WorkoutPlanRepository("workout_plan")

    private val _updateProgress = MutableLiveData(false)
    val updateProgress: LiveData<Boolean> get() = _updateProgress

    fun updateProgress(id: String, progress: Int) {
       launchWithErrorHandling(
           block = {
               workoutPlanRepository.update(
                   id,
                   updates = mapOf("progress" to progress),
                   onComplete = {
                       _updateProgress.postValue(true)
                   },
               )
           }
       )
    }
}