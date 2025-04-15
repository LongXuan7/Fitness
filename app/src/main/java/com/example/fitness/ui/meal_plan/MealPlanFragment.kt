package com.example.fitness.ui.meal_plan

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.Navigation.findNavController
import com.example.fitness.R
import com.example.fitness.data.model.Meal
import com.example.fitness.data.model.MealItem
import com.example.fitness.data.model.MyNutrition
import com.example.fitness.data.model.Sport
import com.example.fitness.data.model.Week
import com.example.fitness.databinding.FragmentMealPlanBinding
import com.example.fitness.ui.nutrition.NutritionFragment
import com.example.fitness.util.base.BaseFragment
import com.example.fitness.util.ext.setAdapterLinearHorizontal
import com.example.fitness.util.ext.setAdapterLinearVertical
import java.time.LocalDate
import java.util.Calendar
import java.util.UUID

class MealPlanFragment : BaseFragment<FragmentMealPlanBinding, MealPlanViewModel>() {

    private var tabSelectedListener: OnTabSelectedListener? = null
    private val adapterDayOfWeek = WeekMealPlanAdapter(::onItemClickWeek, getTodayIndex(getCurrentWeekDays()))
    private var adapter: NutritionMealPlanAdapter? = null
    private var currentCalendar: Calendar = Calendar.getInstance()
    private var mNutrition: List<MyNutrition> = listOf()
    private var mBreakFastList: List<MealItem> = listOf()
    private var mLunchList: List<MealItem> = listOf()
    private var mDinnerList: List<MealItem> = listOf()
    private var mSports: List<Sport> = listOf()
    private var mMeals: List<Meal> = listOf()
    private var mealPlan = "breakfast"
    private var mDate = LocalDate.now().toString()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnTabSelectedListener) {
            tabSelectedListener = context
        }
    }

    override fun onDetach() {
        super.onDetach()
        tabSelectedListener = null
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentMealPlanBinding {
        return FragmentMealPlanBinding.inflate(inflater, container, false)
    }

    override fun setUpViews() {
        binding.tvMonth.text = "Tháng ${LocalDate.now().monthValue}"
        binding.tvYear.text = LocalDate.now().year.toString()
        setUpRecyclerView()
    }

    private fun setUpRecyclerView() {
        binding.rcvDayOfWeek.apply {
            setAdapterLinearHorizontal(adapterDayOfWeek)
            adapterDayOfWeek.submitList(getCurrentWeekDays())
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
        mDate = selectedDate.toString()
        viewModel.getMealPlans(selectedDate.toString())
    }

    override fun setupObservers() {
        viewModel.mealPlanListBreakFas.observe(viewLifecycleOwner) {
            mBreakFastList = it
            trySetupAdapter(compareList(mBreakFastList, mNutrition))
        }
        viewModel.mealPlanListLunch.observe(viewLifecycleOwner) {
            mLunchList = it
        }
        viewModel.mealPlanListDinner.observe(viewLifecycleOwner) {
            mDinnerList = it
        }
        viewModel.myNutritionList.observe(viewLifecycleOwner) {
            mNutrition = it
            trySetupAdapter(compareList(mBreakFastList, mNutrition))
        }
        viewModel.mySportList.observe(viewLifecycleOwner) {
            mSports = it
            trySetupAdapter(compareList(mBreakFastList, mNutrition))
        }
        viewModel.myMealList.observe(viewLifecycleOwner) {
            mMeals = it
            trySetupAdapter(compareList(mBreakFastList, mNutrition))
        }
    }

    private fun compareList(
        list1: List<MealItem>,
        list2: List<MyNutrition>
    ): List<MyNutrition> {
        val nutritionIds = list1.map { it.nutrition_id }.toSet()
        return list2.filter { it.id in nutritionIds }
    }

    override fun setUpOnClick() {
        binding.btnNextTime.setOnClickListener { nextWeek() }
        binding.btnBackTime.setOnClickListener { backWeek() }
        binding.tvBreakfast.setOnClickListener {
            mealPlan = "breakfast"
            setTabSelected(0)
            trySetupAdapter(compareList(mBreakFastList, mNutrition))
        }
        binding.tvLunch.setOnClickListener {
            mealPlan = "lunch"
            setTabSelected(1)
            trySetupAdapter(compareList(mLunchList, mNutrition))
        }
        binding.tvDinner.setOnClickListener {
            mealPlan = "dinner"
            setTabSelected(2)
            trySetupAdapter(compareList(mDinnerList, mNutrition))
        }
        binding.btnAddFood.setOnClickListener {
            val bundle = Bundle().apply {
                putString("mealPlan", mealPlan)
                putString("date", mDate)
            }
            val nutritionFragment = NutritionFragment()
            nutritionFragment.arguments = bundle

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frameLayout_meal_plan, nutritionFragment)
                .commit()

            tabSelectedListener?.onTabSelected(true)
        }
    }

    private fun trySetupAdapter(list: List<MyNutrition>) {
        if (mNutrition.isNotEmpty() && mMeals.isNotEmpty() && mSports.isNotEmpty() && list.isNotEmpty()) {
            adapter = NutritionMealPlanAdapter(
                mMeals = mMeals,
                mSport = mSports,
                onClick = ::onItemClickDelete
            )
            adapter?.let { binding.rcvFood.setAdapterLinearVertical(it) }
            adapter?.submitList(list)
        } else {
            if (list.isEmpty()) {
                adapter?.let { binding.rcvFood.setAdapterLinearVertical(null) }
            }
        }

        if (list.isEmpty() || list.size == 1) {
            binding.tvCountFood.text = requireContext().getString(R.string.count_food, list.size.toString())
        }else {
            binding.tvCountFood.text = requireContext().getString(R.string.count_foods, list.size.toString())
        }
    }

    private fun setTabSelected(isNutritionSelected: Int) {
        when (isNutritionSelected) {
            0 -> {
                setActive(binding.tvBreakfast)
                setInactive(binding.tvLunch)
                setInactive(binding.tvDinner)
            }

            1 -> {
                setInactive(binding.tvBreakfast)
                setActive(binding.tvLunch)
                setInactive(binding.tvDinner)
            }

            2 -> {
                setInactive(binding.tvBreakfast)
                setInactive(binding.tvLunch)
                setActive(binding.tvDinner)
            }
        }
    }

    private fun setActive(view: android.widget.TextView) {
        view.background = ContextCompat.getDrawable(requireContext(), R.drawable.bg_btn_meal_plan)
        view.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
    }

    private fun setInactive(view: android.widget.TextView) {
        view.background =
            ContextCompat.getDrawable(requireContext(), R.drawable.bg_btn_meal_plan_gray)
        view.setTextColor(ContextCompat.getColor(requireContext(), R.color.black))
    }

    private fun onItemClickDelete(myNutrition: MyNutrition) {
//        viewModel.deleteNutrition(myNutrition)
    }
}

interface OnTabSelectedListener {
    fun onTabSelected(isNutritionSelected: Boolean)
}
