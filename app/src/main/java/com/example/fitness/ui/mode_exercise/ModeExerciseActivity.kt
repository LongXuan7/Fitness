package com.example.fitness.ui.mode_exercise

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
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
    override val viewModel: ModeExerciseViewModel by viewModel()
    private var totalTime: Int = 0
    private var remainingSeconds: Int = 0
    private var timePasses: Int = 0

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

        timePasses = workoutPlan?.time_passes ?: 0
        totalTime = (exercise?.time?.toInt() ?: 0) * (workoutPlan?.set ?: 1)
        remainingSeconds = (totalTime - timePasses)
        binding.textView10.text = formatTime(remainingSeconds)

        startVideo(exercise?.video_url)
        startCountdown(remainingSeconds)
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
                    val resultIntent = Intent().apply {
                        putExtra("time_passes", timePasses)
                    }
                    setResult(Activity.RESULT_OK, resultIntent)
                    finish()
                }

                isPaused -> {
                    isPaused = false
                    updateStopButtonState()
                    startCountdown(remainingSeconds)
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
        binding.ivBack.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra("time_passes", timePasses)
            }
            setResult(Activity.RESULT_OK, resultIntent)
            finish()
        }
    }

    override fun setupObservers() {}

    private fun startCountdown(seconds: Int) {
        countDownTimer?.cancel()
        countDownTimer = object : CountDownTimer(seconds * 1000L, 1000L) {
            override fun onTick(millisUntilFinished: Long) {
                remainingSeconds = (millisUntilFinished / 1000).toInt()

                timePasses = totalTime - remainingSeconds
                workoutPlan?.time_passes = timePasses
                val progress = ((timePasses * 100) / totalTime).coerceIn(0, 100)

                binding.textView10.text = formatTime(remainingSeconds)
                workoutPlan?.id?.let { viewModel.updateProgress(it, progress) }
                workoutPlan?.id?.let { viewModel.updateTimePasses(it, timePasses) }
            }

            override fun onFinish() {
                isFinished = true
                binding.textView10.text = formatTime(0)
                workoutPlan?.id?.let { viewModel.updateProgress(it, 100) }
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

    override fun onPause() {
        super.onPause()
        countDownTimer?.cancel()
        val elapsed = totalTime - remainingSeconds
        workoutPlan?.time_passes = elapsed
        val currentProgress = ((elapsed * 100) / totalTime).coerceIn(0, 100)
        workoutPlan?.id?.let { viewModel.updateProgress(it, currentProgress) }
    }

    override fun onDestroy() {
        super.onDestroy()
        countDownTimer?.cancel()
    }
}