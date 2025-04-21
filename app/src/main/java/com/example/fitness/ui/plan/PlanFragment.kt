package com.example.fitness.ui.plan

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.example.fitness.R
import com.example.fitness.data.model.Category
import com.example.fitness.data.model.Exercise
import com.example.fitness.data.model.Week
import com.example.fitness.data.model.WorkoutPlan
import com.example.fitness.databinding.FragmentPlanBinding
import com.example.fitness.ui.add_exercise.AddCategoryActivity
import com.example.fitness.ui.exercise_detail_start.ExerciseDetailStartActivity
import com.example.fitness.util.base.BaseFragment
import com.example.fitness.util.ext.hide
import com.example.fitness.util.ext.setAdapterGrid
import com.example.fitness.util.ext.setAdapterLinearVertical
import com.example.fitness.util.ext.show
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.time.LocalDate
import java.util.Calendar
import java.util.UUID

class PlanFragment : BaseFragment<FragmentPlanBinding, PlanViewModel>() {

    private val adapterDayOfWeek = WeekAdapter(::onItemClickWeek, getTodayIndex(getCurrentWeekDays()))
    private var adapterPlan: WorkoutPlanAdapter? = null
    private var workoutPlans: List<WorkoutPlan> = listOf()
    private var mCategories: List<Category> = listOf()
    private var mExercises: List<Exercise> = listOf()
    private var currentCalendar: Calendar = Calendar.getInstance()
    override val viewModel: PlanViewModel
            by viewModel()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentPlanBinding {
        return FragmentPlanBinding.inflate(inflater, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun setUpViews() {
        binding.tvCountExercise.text = getString(
            R.string.count_exercise,
            workoutPlans.filter { it.time == LocalDate.now().toString() }.size.toString()
        )
        binding.tvMonth.text = "Tháng ${LocalDate.now().monthValue}"
        binding.tvYear.text = LocalDate.now().year.toString()
        setUpRecyclerView()
    }

    override fun setupObservers() {
        viewModel.workoutPlanList.observe(viewLifecycleOwner) {
            workoutPlans = it
            trySetupAdapter()
        }
        viewModel.exercise.observe(viewLifecycleOwner) {
            mExercises = it
            trySetupAdapter()
        }
        viewModel.categories.observe(viewLifecycleOwner) {
            mCategories = it
            trySetupAdapter()
        }
        viewModel.deleteWorkoutPlan.observe(viewLifecycleOwner) {
            if (it) {
                adapterPlan?.submitList(workoutPlans.filter {
                    it.time == LocalDate.now().toString()
                })
                binding.tvCountExercise.text = getString(
                    R.string.count_exercise,
                    workoutPlans.filter { it.time == LocalDate.now().toString() }.size.toString()
                )
            }
        }
    }

    override fun setUpOnClick() {
        binding.btnAddExercise.setOnClickListener {
            startActivity(Intent(requireContext(), AddCategoryActivity::class.java))
        }
        binding.btnNextTime.setOnClickListener { nextWeek() }
        binding.btnBackTime.setOnClickListener { backWeek() }
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
                context,
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
                list.add(Week(id = UUID.randomUUID().toString(), dayOfWeek = dateName, date = date ))
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
            list.add(Week(id = UUID.randomUUID().toString(), dayOfWeek = dateName, date = date ))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return list
    }

    private fun trySetupAdapter() {
        if (workoutPlans.isNotEmpty() && mCategories.isNotEmpty() && mExercises.isNotEmpty()) {
            adapterPlan = WorkoutPlanAdapter(
                mCategories = mCategories,
                mExercises = mExercises,
                ::onItemClickItem,
                ::onItemClickDelete
            )

            binding.rcvExercisePlan.apply {
                setAdapterLinearVertical(adapterPlan!!)
            }
            adapterPlan?.submitList(workoutPlans.filter { it.time == LocalDate.now().toString() })
            binding.tvCountExercise.text = getString(
                R.string.count_exercise,
                workoutPlans.filter { it.time == LocalDate.now().toString() }.size.toString()
            )
        }
    }

    private fun setUpRecyclerView() {
        binding.rcvDayOfWeek.apply {
            setAdapterGrid(adapterDayOfWeek, 7)
            adapterDayOfWeek.submitList(getCurrentWeekDays())
        }
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
        adapterPlan?.submitList(workoutPlans.filter {
            it.time == LocalDate.of(year, month, date).toString()
        })
        binding.tvCountExercise.text = getString(
            R.string.count_exercise,
            workoutPlans.filter {
                it.time == LocalDate.of(year, month, date).toString()
            }.size.toString()
        )

        val itemDate = LocalDate.of(year, month, date)
        val currentDate = LocalDate.now()

        if (!itemDate.isBefore(currentDate)) {
            binding.btnAddExercise.show()
        } else {
            binding.btnAddExercise.hide()
        }
    }

    private fun onItemClickDelete(workoutPlan: WorkoutPlan) {
        val dialog = AlertDialog.Builder(requireContext())
        dialog.setTitle("Xóa kế hoạch tập luyện")
        dialog.setMessage("Bạn có chắc chắn muốn xóa kế hoạch tập luyện này không?")
        dialog.setPositiveButton("Xóa") { _, _ ->
            viewModel.deleteWorkoutPlan(workoutPlan.id.toString())
        }
        dialog.setNegativeButton("Huy") { _, _ -> }
        dialog.show()
    }

    private fun onItemClickItem(workoutPlan: WorkoutPlan) {
        val intent = Intent(requireContext(), ExerciseDetailStartActivity::class.java)
        intent.putExtra("exercise", mExercises.find { it.id == workoutPlan.exercise_id })
        intent.putExtra(
            "title",
            mCategories.find { it.id == mExercises.find { it.id == workoutPlan.exercise_id }!!.category_id })
        intent.putExtra("workoutPlan", workoutPlan)
        startActivity(intent)
    }
}