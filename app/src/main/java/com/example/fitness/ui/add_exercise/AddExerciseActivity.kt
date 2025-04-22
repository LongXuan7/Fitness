package com.example.fitness.ui.add_exercise

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fitness.R
import com.example.fitness.data.model.Category
import com.example.fitness.data.model.Exercise
import com.example.fitness.data.model.Reminder
import com.example.fitness.databinding.ActivityAddExerciseBinding
import com.example.fitness.ui.exercise.CategoryAdapter
import com.example.fitness.ui.exercise.ExerciseAdapter
import com.example.fitness.ui.exercise_detail.ExerciseDetailActivity
import com.example.fitness.util.base.BaseActivity
import com.example.fitness.util.ext.hide
import com.example.fitness.util.ext.setAdapterLinearVertical
import com.example.fitness.util.ext.show
import com.google.gson.Gson
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddExerciseActivity : BaseActivity<ActivityAddExerciseBinding, AddExerciseViewModel>() {

    private val adapterExercise = AddExerciseAdapter(::onItemExerciseClick, ::onItemCount, ::onClickUpdate)
    private var mExerciseSelected = mutableListOf<Exercise>()
    private var mCategory: Category? = null
    override val viewModel: AddExerciseViewModel
            by viewModel()

    override val bindingInflater: (LayoutInflater) -> ActivityAddExerciseBinding
        get() = ActivityAddExerciseBinding::inflate

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun setupViews() {
        setUpRecyclerViewExercise()
        mCategory = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getParcelableExtra("category", Category::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("category") as? Category
        }
        viewModel.fetchExercise(mCategory?.id)
    }

    override fun setupOnClick() {
        binding.ivBackExercise.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun setupObservers() {
        viewModel.exercise.observe(this) {
            adapterExercise.submitList(it)
        }
    }

    private fun setUpRecyclerViewExercise() {
        binding.rcvExercise.apply {
            setAdapterLinearVertical(adapterExercise)
        }
    }

    private fun onItemExerciseClick(exercise: Exercise) {
        val intent = Intent(this, ExerciseDetailActivity::class.java)
        intent.putExtra("exercise", exercise)
        intent.putExtra("title", mCategory?.name)
        startActivity(intent)
    }

    @SuppressLint("SetTextI18n")
    private fun onItemCount(exerciseList: List<Exercise>) {
        mExerciseSelected = exerciseList.toMutableList()
        binding.tvCount.text = getString(R.string.count, exerciseList.size.toString())
        viewModel.updateCountTemp(mCategory?.id, exerciseList.size.toString())

        exerciseList.forEach {
            viewModel.updateCountTempExercise(it.id, it.set_temp)
            Log.d("longnx", "onItemCount: ${it.set_temp}")
        }



        getSharedPreferences("EXERCISES", Context.MODE_PRIVATE)
            .edit()
            .putString("exercise", Gson().toJson(exerciseList))
            .apply()
    }

    private fun onClickUpdate(exercise: Exercise) {
        viewModel.updateAllSetTemp(exercise.id)
    }

}