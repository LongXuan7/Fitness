package com.example.fitness.ui.exercise_detail

import android.net.Uri
import android.os.Build
import android.view.LayoutInflater
import androidx.annotation.RequiresApi
import com.example.fitness.R
import com.example.fitness.data.model.Exercise
import com.example.fitness.data.model.GuideStep
import com.example.fitness.data.model.Tag
import com.example.fitness.databinding.ActivityExerciseDetailBinding
import com.example.fitness.util.FormatTime.formatTime
import com.example.fitness.util.base.BaseActivity
import com.example.fitness.util.ext.hide
import com.example.fitness.util.ext.setAdapterGrid
import com.example.fitness.util.ext.setAdapterLinearVertical
import com.example.fitness.util.ext.show
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExerciseDetailActivity : BaseActivity<ActivityExerciseDetailBinding, ExerciseDetailViewModel>() {

    private val adapterTag = TagAdapter()
    private val adapterGuideStep = GuideStepAdapter()
    override val viewModel: ExerciseDetailViewModel
            by viewModel()

    override val bindingInflater: (LayoutInflater) -> ActivityExerciseDetailBinding
        get() = { inflater ->
            ActivityExerciseDetailBinding.inflate(inflater)
        }

    override fun setupViews() {
        val exercise = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("exercise", Exercise::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("exercise") as? Exercise
        }

        val title = intent.getStringExtra("title")
        title?.let {
            binding.tvTitleExercise.text = title
        }
        exercise?.let {
            binding.tvTitleExerciseDetail.text = it.title
            binding.tvDescriptionExerciseDetail.text = it.description
            binding.tvCaloExerciseDetail.text = getString(R.string.calories, it.calories)
            binding.tvTimerExerciseDetail.text = getString(R.string.timer, formatTime(it.time.toInt()))

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
    }

    override fun setupObservers() {

    }
}