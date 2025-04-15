package com.example.fitness.ui.add_reminder

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fitness.R
import com.example.fitness.data.model.Week
import com.example.fitness.databinding.ActivityAddReminderBinding
import com.example.fitness.ui.meal_plan.WeekMealPlanAdapter
import com.example.fitness.util.base.BaseActivity
import com.example.fitness.util.ext.setAdapterLinearHorizontal
import java.time.LocalDate
import java.util.Calendar
import java.util.UUID

class AddReminderActivity : BaseActivity<ActivityAddReminderBinding, AddReminderViewModel>() {

    private val adapterDayOfWeek = WeekMealPlanAdapter(::onItemClickWeek, getTodayIndex(getCurrentWeekDays()))
    private var currentCalendar: Calendar = Calendar.getInstance()

    override val bindingInflater: (LayoutInflater) -> ActivityAddReminderBinding
        get() = ActivityAddReminderBinding::inflate

    override fun setupViews() {
        binding.tvMonth.text = "Tháng ${LocalDate.now().monthValue}"
        binding.tvYear.text = LocalDate.now().year.toString()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.rcvWeek.apply {
            setAdapterLinearHorizontal(adapterDayOfWeek)
            adapterDayOfWeek.submitList(getCurrentWeekDays())
        }
    }

    override fun setupOnClick() {
        binding.btnNextTime.setOnClickListener { nextWeek() }
        binding.btnBackTime.setOnClickListener { backWeek() }
    }

    override fun setupObservers() {

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
        adapterDayOfWeek.setSelectedPosition(-1)
        adapterDayOfWeek.submitList(weekDays)
    }

    private fun getWeekDays(): List<Week> {
        val tempCalendar = currentCalendar.clone() as Calendar
        val currentMonth = tempCalendar.get(Calendar.MONTH)
        val list = mutableListOf<Week>()
        val thuMap = mapOf(
            Calendar.MONDAY to "Mon",
            Calendar.TUESDAY to "Tus",
            Calendar.WEDNESDAY to "Wen",
            Calendar.THURSDAY to "Thu",
            Calendar.FRIDAY to "Fri",
            Calendar.SATURDAY to "Sat",
            Calendar.SUNDAY to "Sun"
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
                list.add(Week(id = UUID.randomUUID().toString(), dayOfWeek = dateName, date = date))
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
            Calendar.MONDAY to "Mon",
            Calendar.TUESDAY to "Tus",
            Calendar.WEDNESDAY to "Wen",
            Calendar.THURSDAY to "Thu",
            Calendar.FRIDAY to "Fri",
            Calendar.SATURDAY to "Sat",
            Calendar.SUNDAY to "Sun"
        )

        for (i in 0 until 7) {
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            val dateName = thuMap[dayOfWeek] ?: "?"
            val date = calendar.get(Calendar.DAY_OF_MONTH).toString()
            list.add(Week(id = UUID.randomUUID().toString(), dayOfWeek = dateName, date = date))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return list
    }

    private fun getTodayIndex(weekList: List<Week>): Int {
        val today = Calendar.getInstance()
        val todayDate = today.get(Calendar.DAY_OF_MONTH).toString()
        return weekList.indexOfFirst { it.date == todayDate }
    }

    private fun onItemClickWeek(week: Week) {
        val date = week.date.toInt()
        val month = binding.tvMonth.text.toString().replace("Tháng ", "").toInt()
        val year = binding.tvYear.text.toString().toInt()
        val selectedDate = LocalDate.of(year, month, date)

    }

}