package com.example.fitness.ui.add_exercise

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fitness.R
import com.example.fitness.data.model.Category
import com.example.fitness.data.model.Exercise
import com.example.fitness.data.model.WorkoutPlan
import com.example.fitness.databinding.ActivityAddCategoryBinding
import com.example.fitness.databinding.ActivityAddExerciseBinding
import com.example.fitness.ui.exercise_detail.ExerciseDetailActivity
import com.example.fitness.util.base.BaseActivity
import com.example.fitness.util.ext.setAdapterLinearVertical
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.time.LocalDate
import java.util.UUID

class AddCategoryActivity : BaseActivity<ActivityAddCategoryBinding, AddExerciseViewModel>() {

    private val adapterCategory = AddCategoryAdapter(::onItemCategoryClick)
    private var mCategories = mutableListOf<Category>()

    override val bindingInflater: (LayoutInflater) -> ActivityAddCategoryBinding
        get() = ActivityAddCategoryBinding::inflate

    override fun setupViews() {
        setUpRecyclerView()
    }

    override fun setupOnClick() {
        binding.ivBackExercise.setOnClickListener {
            resetAllCountTemp()
            resetAllSetTemp()
            onBackPressedDispatcher.onBackPressed()
        }
        binding.tvCancel.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        binding.tvAdd.setOnClickListener {addPlanExercise()}
    }

    @SuppressLint("SetTextI18n")
    override fun setupObservers() {
        viewModel.categories.observe(this) { categories ->
            var mCount = 0
            mCategories = categories.toMutableList()

            categories.forEach {
                mCount += it.count_temp ?: 0
            }
            adapterCategory.submitList(categories)
            binding.tvCount.text = getString(R.string.count, mCount.toString())
        }
        viewModel.addWorkoutPlan.observe(this) {
            if (it) {
                resetAllCountTemp()
                resetAllSetTemp()
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun setUpRecyclerView() {
        binding.rcvExercise.apply {
            setAdapterLinearVertical(adapterCategory)
        }
    }

    private fun onItemCategoryClick(category: Category) {
        startActivity(Intent(this, AddExerciseActivity::class.java).apply {
            putExtra("category", category)
        })
    }


    private fun resetAllCountTemp() {
        mCategories.forEach {
            viewModel.updateAllCountTemp(it.id)
        }
        Log.d("longnx", "resetAllCountTemp: $mCategories")
    }

    private fun resetAllSetTemp() {
        val json =  getSharedPreferences("EXERCISES", Context.MODE_PRIVATE).getString("exercise", "")
        val type = object : TypeToken<List<Exercise>>() {}.type
        val list = Gson().fromJson<List<Exercise>>(json, type)
        list?.forEach {
            viewModel.updateAllSetTemp(it.id)
        }
    }

    private fun addPlanExercise() {
        val json = getSharedPreferences("EXERCISES", Context.MODE_PRIVATE).getString("exercise", "")
        val type = object : TypeToken<List<Exercise>>() {}.type
        val list = Gson().fromJson<List<Exercise>>(json, type)
        list?.forEach {
            val workoutPlan = WorkoutPlan(
                id = UUID.randomUUID().toString(),
                time = LocalDate.now().toString(),
                exercise_id = it.id,
                user_id = 1,
                set = it.set_temp,
                completedSet = 0,
                progress = 0
            )
            viewModel.addWorkoutPlan(workoutPlan)
        }
    }
}