package com.example.fitness.ui.sport

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import com.example.fitness.R
import com.example.fitness.data.model.Sport
import com.example.fitness.data.model.SportCategory
import com.example.fitness.databinding.ActivitySportBinding
import com.example.fitness.ui.meal_detail.MealDetailActivity
import com.example.fitness.ui.sport_detail.SportDetailActivity
import com.example.fitness.util.base.BaseActivity
import com.example.fitness.util.ext.setAdapterLinearVertical
import org.koin.androidx.viewmodel.ext.android.viewModel

class SportActivity : BaseActivity<ActivitySportBinding, SportViewModel>() {

    private val adapterSportCategory = SportCategoryAdapter(::onItemClickSportCategory)
    private val adapterSport = SportAdapter(::onItemClickSport)

    private var isBack = false
    override val viewModel: SportViewModel
            by viewModel()

    override val bindingInflater: (LayoutInflater) -> ActivitySportBinding
        get() = ActivitySportBinding::inflate

    override fun setupViews() {
        binding.headerTitle.tvTitle.text = getString(R.string.sport_category)
        setUpRecyclerViewSportCategory()
    }

    private fun setUpRecyclerViewSportCategory() {
        binding.rcvMealCategory.setAdapterLinearVertical(adapterSportCategory)
    }

    private fun setUpRecyclerViewSport() {
        binding.rcvMealCategory.setAdapterLinearVertical(adapterSport)
    }

    override fun setupOnClick() {
        binding.headerTitle.ivBack.setOnClickListener {
            if (isBack) {
                setUpRecyclerViewSportCategory()
                binding.etSearch.text?.clear()
                isBack = false
            } else {
                onBackPressedDispatcher.onBackPressed()
            }
        }

        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()

                if (isBack) {
                    adapterSport.filter(query)
                } else {
                    adapterSportCategory.filter(query)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })

    }

    override fun setupObservers() {
        viewModel.sportCategories.observe(this) {
            adapterSportCategory.submitList(it)
        }
        viewModel.sport.observe(this) {
            adapterSport.submitList(it)
        }
    }

    private fun onItemClickSportCategory(sportCategory: SportCategory) {
        setUpRecyclerViewSport()
        viewModel.getMeals(sportCategory.id)
        isBack = true
    }

    private fun onItemClickSport(sport: Sport) {
        startActivity(Intent(this, SportDetailActivity::class.java).apply {
            putExtra("sport", sport)
        })
    }
}