package com.example.fitness.ui.exercise_detail_start

import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import com.example.fitness.R
import com.example.fitness.data.model.Exercise
import com.example.fitness.data.model.GuideStep
import com.example.fitness.data.model.Tag
import com.example.fitness.data.model.WorkoutPlan
import com.example.fitness.databinding.ActivityExerciseDetailStartBinding
import com.example.fitness.ui.exercise_detail.ExerciseDetailViewModel
import com.example.fitness.ui.exercise_detail.GuideStepAdapter
import com.example.fitness.ui.exercise_detail.TagAdapter
import com.example.fitness.ui.mode_exercise.ModeExerciseActivity
import com.example.fitness.util.FormatTime.formatTime
import com.example.fitness.util.base.BaseActivity
import com.example.fitness.util.ext.hide
import com.example.fitness.util.ext.setAdapterGrid
import com.example.fitness.util.ext.setAdapterLinearVertical
import com.example.fitness.util.ext.show

class ExerciseDetailStartActivity :
    BaseActivity<ActivityExerciseDetailStartBinding, ExerciseDetailViewModel>() {

    private val adapterTag = TagAdapter()
    private val adapterGuideStep = GuideStepAdapter()
    private var exercise: Exercise? = null
    private var title: String? = null
    private var workoutPlan: WorkoutPlan? = null


    override val bindingInflater: (LayoutInflater) -> ActivityExerciseDetailStartBinding
        get() = { inflater ->
            ActivityExerciseDetailStartBinding.inflate(inflater)
        }

    override fun setupViews() {
        exercise = intent.getSerializableExtra("exercise", Exercise::class.java)
        title = intent.getStringExtra("title")
        workoutPlan = intent.getSerializableExtra("workoutPlan", WorkoutPlan::class.java)

        title?.let {
            binding.tvTitleExercise.text = title
        }
        exercise?.let {
            binding.tvTitleExerciseDetail.text = it.title
            binding.tvDescriptionExerciseDetail.text = it.description
            binding.tvCaloExerciseDetail.text = getString(R.string.calories, it.calories)
            binding.tvTimerExerciseDetail.text =
                getString(R.string.timer, formatTime(it.time.toInt()))

            val cleanGuideSteps = if (it.guide_steps.firstOrNull() == null)
                it.guide_steps.drop(1)
            else
                it.guide_steps

            val cleanTags = if (it.tags.firstOrNull() == null)
                it.tags.drop(1)
            else
                it.tags

            setUpRecyclerViewTag(cleanTags)
            setUpRecyclerViewGuideStep(cleanGuideSteps)
        }

        val uri = Uri.parse(exercise?.video_url)
        binding.videoView.setVideoURI(uri)
        binding.videoView.setOnPreparedListener { mediaPlayer ->
            binding.progressBar2.show()
            mediaPlayer.isLooping = true
            binding.videoView.start()
            binding.progressBar2.hide()
        }
        binding.videoView.setOnErrorListener { _, _, _ ->
            binding.progressBar2.hide()
            false
        }
    }

    private fun setUpRecyclerViewGuideStep(guideSteps: List<GuideStep>) {
        binding.rcvGuideStepExerciseDetail.apply {
            setAdapterLinearVertical(adapterGuideStep)
            adapterGuideStep.submitList(guideSteps)
        }
    }

    private fun setUpRecyclerViewTag(tags: List<Tag>) {
        binding.rcvTagExerciseDetail.apply {
            setAdapterGrid(adapterTag, 3)
            adapterTag.submitList(tags)
        }
    }

    override fun setupOnClick() {
        binding.ivBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.tvStart.setOnClickListener {
            exercise?.let {
                startActivity(Intent(this, ModeExerciseActivity::class.java).apply {
                    putExtra("exercise", it)
                    putExtra("title", title)
                    putExtra("workoutPlan", workoutPlan)
                })
            }
        }
    }

    override fun setupObservers() {

    }
}