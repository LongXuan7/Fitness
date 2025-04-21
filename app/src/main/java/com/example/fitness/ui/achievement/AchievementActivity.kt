package com.example.fitness.ui.achievement


import android.annotation.SuppressLint
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.widget.Toast
import com.example.fitness.data.model.Week
import com.example.fitness.data.model.WorkoutPlan
import com.example.fitness.databinding.ActivityAchievementBinding
import com.example.fitness.ui.achievement_detail.AchievementDetailActivity
import com.example.fitness.util.base.BaseActivity
import com.example.fitness.util.ext.setAdapterLinearVertical
import com.google.gson.Gson
import java.util.Calendar
import java.util.UUID
import org.koin.androidx.viewmodel.ext.android.viewModel

class AchievementActivity : BaseActivity<ActivityAchievementBinding, AchievementViewModel>() {

    private val achievementAdapter = AchievementAdapter(::onItemClickWeek)
    private var currentCalendar: Calendar = Calendar.getInstance()
    private var workoutPlanList: List<WorkoutPlan> = emptyList()
    override val viewModel: AchievementViewModel by viewModel()

    override val bindingInflater: (LayoutInflater) -> ActivityAchievementBinding
        get() = ActivityAchievementBinding::inflate

    override fun setupViews() {
    }

    override fun setupOnClick() {
        binding.ivBack.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.btnNextTime.setOnClickListener { nextWeek() }
        binding.btnBackTime.setOnClickListener { backWeek() }
    }

    @SuppressLint("SetTextI18n")
    override fun setupObservers() {
        viewModel.workoutPlanList.observe(this) {
            achievementAdapter.setData(it)
            workoutPlanList = it
            setUpRecyclerView()
        }
        viewModel.exercise.observe(this) { exercise ->
            val totalCalo = workoutPlanList
                .filter { it.progress == 100 }
                .sumOf { workout ->
                    exercise.find { it.id == workout.exercise_id }?.calories?.toIntOrNull() ?: 0
                }
            binding.textView13.text = totalCalo.toString()
        }
    }

    private fun isSameMonth(): Boolean {
        val currentMonth = currentCalendar.get(Calendar.MONTH)
        val currentYear = currentCalendar.get(Calendar.YEAR)
        val currentMonthNow = Calendar.getInstance().get(Calendar.MONTH)
        val currentYearNow = Calendar.getInstance().get(Calendar.YEAR)
        return currentMonth == currentMonthNow && currentYear == currentYearNow
    }

    private fun backWeek() {
        val tempCalendar = currentCalendar.clone() as Calendar
        tempCalendar.add(Calendar.WEEK_OF_YEAR, -1)

        val firstDayOfMonth = tempCalendar.clone() as Calendar
        firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1)

        if (tempCalendar.get(Calendar.DAY_OF_MONTH) > 7 &&
            tempCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)
        ) {
            currentCalendar.add(Calendar.WEEK_OF_YEAR, -1)
            updateWeekList()
        } else {
            currentCalendar.set(Calendar.DAY_OF_MONTH, 1)
            updateWeekList()
        }
    }

    private fun nextWeek() {
        currentCalendar.add(Calendar.WEEK_OF_YEAR, 1)
        if (isSameMonth()) {
            updateWeekList()
        } else {
            Toast.makeText(
                this,
                "Không thể chuyển sang tuần sau trong tháng này",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun updateWeekList() {
        val weekDays = getWeekDays()
        achievementAdapter.submitList(weekDays)
    }

    private fun getWeekDays(): List<Week> {
        val tempCalendar = currentCalendar.clone() as Calendar
        val currentMonth = tempCalendar.get(Calendar.MONTH)
        val list = mutableListOf<Week>()
        val thuMap = mapOf(
            Calendar.MONDAY to "T2",
            Calendar.TUESDAY to "T3",
            Calendar.WEDNESDAY to "T4",
            Calendar.THURSDAY to "T5",
            Calendar.FRIDAY to "T6",
            Calendar.SATURDAY to "T7",
            Calendar.SUNDAY to "CN"
        )

        if (tempCalendar.get(Calendar.DAY_OF_MONTH) <= 7) {
            tempCalendar.set(Calendar.DAY_OF_MONTH, 1)
        } else {
            tempCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        }

        for (i in 0 until 7) {
            val month = tempCalendar.get(Calendar.MONTH)
            if (month == currentMonth) {
                val dayOfWeek = tempCalendar.get(Calendar.DAY_OF_WEEK)
                val dateName = thuMap[dayOfWeek] ?: "?"
                val date = tempCalendar.get(Calendar.DAY_OF_MONTH).toString()
                list.add(Week(id = UUID.randomUUID().toString(), dayOfWeek = dateName, date = date,
                    month = (month + 1).toString(), year = tempCalendar.get(Calendar.YEAR).toString()))
            }
            tempCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return list
    }

    private fun getCurrentWeekDays(): List<Week> {
        val calendar = Calendar.getInstance()

        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        val list = mutableListOf<Week>()
        val thuMap = mapOf(
            Calendar.MONDAY to "T2",
            Calendar.TUESDAY to "T3",
            Calendar.WEDNESDAY to "T4",
            Calendar.THURSDAY to "T5",
            Calendar.FRIDAY to "T6",
            Calendar.SATURDAY to "T7",
            Calendar.SUNDAY to "CN"
        )

        for (i in 0 until 7) {
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            val dateName = thuMap[dayOfWeek] ?: "?"
            val date = calendar.get(Calendar.DAY_OF_MONTH).toString()
            list.add(Week(id = UUID.randomUUID().toString(), dayOfWeek = dateName, date = date,
                month = (calendar.get(Calendar.MONTH) + 1).toString(), year = calendar.get(Calendar.YEAR).toString()))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return list
    }

    private fun setUpRecyclerView() {
        binding.rcvAchievement.apply {
            setAdapterLinearVertical(achievementAdapter)
            achievementAdapter.submitList(getCurrentWeekDays())
        }
    }

    private fun onItemClickWeek(item: List<WorkoutPlan>) {
        startActivity(Intent(this, AchievementDetailActivity::class.java).apply {
            putExtra("workoutPlan", Gson().toJson(item))
        })
    }
}