package com.example.fitness.ui.achievement_detail

import android.view.LayoutInflater
import com.example.fitness.R
import com.example.fitness.data.model.Category
import com.example.fitness.data.model.Exercise
import com.example.fitness.data.model.WorkoutPlan
import com.example.fitness.databinding.ActivityAchievementDetailBinding
import com.example.fitness.util.base.BaseActivity
import com.example.fitness.util.ext.setAdapterLinearVertical
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.koin.androidx.viewmodel.ext.android.viewModel

class AchievementDetailActivity : BaseActivity<ActivityAchievementDetailBinding, AchievementDetailViewModel>() {

    private var adapterPlan: WorkoutPlanAchievementAdapter? = null
    private var workoutPlans: List<WorkoutPlan> = listOf()
    private var mCategories: List<Category> = listOf()
    private var mExercises: List<Exercise> = listOf()
    override val viewModel: AchievementDetailViewModel by viewModel()

    override val bindingInflater: (LayoutInflater) -> ActivityAchievementDetailBinding
        get() = ActivityAchievementDetailBinding::inflate

    override fun setupViews() {
        val json = intent.getStringExtra("workoutPlan")
        val type = object : TypeToken<List<WorkoutPlan>>() {}.type
        workoutPlans = Gson().fromJson(json, type)

        binding.tvCountArDetail.text = getString(
            R.string.count_exercise,
            workoutPlans.size.toString()
        )
    }

    override fun setupOnClick() {
        binding.ivBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    override fun setupObservers() {
        viewModel.exercise.observe(this) {
            mExercises = it
            trySetupAdapter()
        }
        viewModel.categories.observe(this) {
            mCategories = it
            trySetupAdapter()
        }
    }

    private fun trySetupAdapter() {
        if (workoutPlans.isNotEmpty() && mCategories.isNotEmpty() && mExercises.isNotEmpty()) {
            adapterPlan = WorkoutPlanAchievementAdapter(
                mCategories = mCategories,
                mExercises = mExercises,
            )

            binding.rcvArDetail.apply {
                setAdapterLinearVertical(adapterPlan!!)
            }
            adapterPlan?.submitList(workoutPlans)
            binding.tvCountArDetail.text = getString(
                R.string.count_exercise,
                workoutPlans.size.toString()
            )
        }
    }

}