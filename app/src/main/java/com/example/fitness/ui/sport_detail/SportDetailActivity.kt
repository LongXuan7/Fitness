package com.example.fitness.ui.sport_detail

import android.view.LayoutInflater
import android.widget.Toast
import com.example.fitness.data.model.MyNutrition
import com.example.fitness.data.model.Sport
import com.example.fitness.databinding.ActivitySportDetailBinding
import com.example.fitness.ui.sport.BenefitAdapter
import com.example.fitness.ui.sport.ImpactAdapter
import com.example.fitness.util.base.BaseActivity
import com.example.fitness.util.ext.loadImage
import com.example.fitness.util.ext.setAdapterLinearVertical
import java.util.UUID

class SportDetailActivity : BaseActivity<ActivitySportDetailBinding, SportDetailViewModel>() {

    private var sport: Sport? = null
    private val adapterBenefit = BenefitAdapter()
    private val adapterImpact = ImpactAdapter()

    override val bindingInflater: (LayoutInflater) -> ActivitySportDetailBinding
        get() = ActivitySportDetailBinding::inflate

    override fun setupViews() {
        sport = intent.getSerializableExtra("sport", Sport::class.java)
        setData()
    }

    private fun setData() {
        sport?.let {
            binding.tvTitleSportDetail.text = it.title
            binding.tvDescriptionSportDetail.text = it.description
            binding.ivSportDetail.loadImage(it.image)

            setUpRecyclerView()

            val cleanBenefit = if (it.benefit?.firstOrNull() == null)
                it.benefit?.drop(1)
            else
                it.benefit

            val cleanImpact = if (it.impact?.firstOrNull() == null)
                it.impact?.drop(1)
            else
                it.impact

            adapterBenefit.submitList(cleanBenefit)
            adapterImpact.submitList(cleanImpact)
        }
    }

    private fun setUpRecyclerView() {
        binding.rcvBenefit.setAdapterLinearVertical(adapterBenefit)
        binding.rcvImpact.setAdapterLinearVertical(adapterImpact)
    }

    override fun setupOnClick() {
        binding.ivBackSportDetail.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.tvAddSportPlan.setOnClickListener { viewModel.addMyNutrition(
            MyNutrition(
            id = UUID.randomUUID().toString(),
            meal_id = "",
            sport_id = sport?.id.toString(),
            user_id = "1"
        )
        ) }
    }

    override fun setupObservers() {
        viewModel.addMyNutrition.observe(this) {
            if (it) {
                Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show()
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }
}