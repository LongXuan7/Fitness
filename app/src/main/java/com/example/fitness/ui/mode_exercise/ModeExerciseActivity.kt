package com.example.fitness.ui.mode_exercise

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fitness.R
import com.example.fitness.data.model.Exercise
import com.example.fitness.data.model.Reminder
import com.example.fitness.data.model.WorkoutPlan
import com.example.fitness.databinding.ActivityModeExerciseBinding
import com.example.fitness.ui.exercise_detail_start.ModeExerciseViewModel
import com.example.fitness.util.FormatTime.formatTime
import com.example.fitness.util.base.BaseActivity
import com.example.fitness.util.ext.hide
import com.example.fitness.util.ext.show
import org.koin.androidx.viewmodel.ext.android.viewModel

class ModeExerciseActivity : BaseActivity<ActivityModeExerciseBinding, ModeExerciseViewModel>() {

    private var countDownTimer: CountDownTimer? = null
    private var isPaused = false
    private var isFinished = false
    private var timeLeftInSeconds = 0
    private var exercise: Exercise? = null
    private var workoutPlan: WorkoutPlan? = null
    override val viewModel: ModeExerciseViewModel
            by viewModel()

    override val bindingInflater: (LayoutInflater) -> ActivityModeExerciseBinding
        get() = ActivityModeExerciseBinding::inflate

    override fun setupViews() {
        exercise = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("exercise", Exercise::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("exercise") as? Exercise
        }

        workoutPlan = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("workoutPlan", WorkoutPlan::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("workoutPlan") as? WorkoutPlan
        }

        startVideo(exercise?.video_url)
        timeLeftInSeconds = exercise?.time?.toInt() ?: 0
        binding.textView10.text = formatTime(timeLeftInSeconds)
        startCountdown(timeLeftInSeconds)
    }

    private fun startVideo(videoUrl: String?) {
        val uri = Uri.parse(videoUrl)
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

    override fun setupOnClick() {
        binding.tvStop.setOnClickListener {
            when {
                isFinished -> {
                    onBackPressedDispatcher.onBackPressed()
                }
                isPaused -> {
                    isPaused = false
                    updateStopButtonState()
                    startCountdown(timeLeftInSeconds)
                    binding.videoView.start()
                }
                else -> {
                    isPaused = true
                    countDownTimer?.cancel()
                    binding.videoView.pause()
                    updateStopButtonState()
                }
            }
        }
        binding.ivBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    override fun setupObservers() {}

    private fun startCountdown(seconds: Int) {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(seconds * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                timeLeftInSeconds = (millisUntilFinished / 1000).toInt()
                binding.textView10.text = formatTime(timeLeftInSeconds)

                val totalSet = workoutPlan?.set ?: 0
                val eachSetTime = exercise?.time?.toIntOrNull() ?: 0
                val elapsedTimeThisSet = eachSetTime - timeLeftInSeconds
                val completedSetBefore = workoutPlan?.progress!!

                val progressPercent = calculateProgressPercent(
                    totalSet,
                    eachSetTime,
                    elapsedTimeThisSet,
                    completedSetBefore
                )
                workoutPlan?.id?.let { viewModel.updateProgress(it,progressPercent) }
            }

            override fun onFinish() {
                isFinished = true
                binding.textView10.text = formatTime(0)
                binding.videoView.pause()
                updateStopButtonState()
            }
        }.start()
    }

    @SuppressLint("SetTextI18n")
    private fun updateStopButtonState() {
        when {
            isFinished -> {
                binding.tvStop.setBackgroundResource(R.drawable.bg_done)
                binding.tvStop.text = "Hoàn thành"
            }
            isPaused -> {
                binding.tvStop.setBackgroundResource(R.drawable.bg_resume)
                binding.tvStop.text = "Tiếp tục"
            }
            else -> {
                binding.tvStop.setBackgroundResource(R.drawable.bg_stop)
                binding.tvStop.text = "Dừng"
            }
        }
    }

    fun calculateProgressPercent(
        totalSet: Int,
        eachSetTime: Int,
        elapsedTimeThisSet: Int,
        completedSetBefore: Int = 0
    ): Int {
        val totalTime = totalSet * eachSetTime
        val totalElapsed = completedSetBefore * eachSetTime + elapsedTimeThisSet
        return if (totalTime > 0) (totalElapsed * 100) / totalTime else 0
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}