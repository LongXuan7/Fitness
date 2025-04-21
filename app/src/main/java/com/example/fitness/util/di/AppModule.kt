package com.example.fitness.util.di

import android.content.Context
import android.content.SharedPreferences
import com.example.fitness.ui.achievement.AchievementViewModel
import com.example.fitness.ui.achievement_detail.AchievementDetailViewModel
import com.example.fitness.ui.add_exercise.AddExerciseViewModel
import com.example.fitness.ui.add_reminder.AddReminderViewModel
import com.example.fitness.ui.exercise.CategoryViewmodel
import com.example.fitness.ui.exercise_detail.ExerciseDetailViewModel
import com.example.fitness.ui.exercise_detail_start.ModeExerciseViewModel
import com.example.fitness.ui.food.FoodViewModel
import com.example.fitness.ui.home.HomeViewModel
import com.example.fitness.ui.meal.MealViewModel
import com.example.fitness.ui.meal_detail.MealDetailViewModel
import com.example.fitness.ui.meal_plan.MealPlanViewModel
import com.example.fitness.ui.my_meal_plan.MyMealPlanViewModel
import com.example.fitness.ui.nutrition.NutritionViewModel
import com.example.fitness.ui.plan.PlanViewModel
import com.example.fitness.ui.profile.ProfileViewModel
import com.example.fitness.ui.reminder.ReminderViewModel
import com.example.fitness.ui.setting.SettingViewModel
import com.example.fitness.ui.sport.SportViewModel
import com.example.fitness.ui.sport_detail.SportDetailViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

    single<SharedPreferences> {
        androidContext().getSharedPreferences("settings", Context.MODE_PRIVATE)
    }

    viewModel { AddExerciseViewModel(sharedPref = get()) }
    viewModel { AchievementViewModel(sharedPref = get()) }
    viewModel { AchievementDetailViewModel(sharedPref = get()) }
    viewModel { AddReminderViewModel(sharedPref = get()) }
    viewModel { CategoryViewmodel(sharedPref = get()) }
    viewModel { ModeExerciseViewModel() }
    viewModel { ExerciseDetailViewModel() }
    viewModel { FoodViewModel() }
    viewModel { HomeViewModel() }
    viewModel { MealViewModel(sharedPref = get()) }
    viewModel { MealDetailViewModel(sharedPref = get()) }
    viewModel { MealPlanViewModel(sharedPref = get()) }
    viewModel { MyMealPlanViewModel() }
    viewModel { NutritionViewModel(sharedPref = get()) }
    viewModel { PlanViewModel(sharedPref = get()) }
    viewModel { ProfileViewModel() }
    viewModel { ReminderViewModel(sharedPref = get()) }
    viewModel { SettingViewModel() }
    viewModel { SportViewModel(sharedPref = get()) }
    viewModel { SportDetailViewModel(sharedPref = get()) }
}
