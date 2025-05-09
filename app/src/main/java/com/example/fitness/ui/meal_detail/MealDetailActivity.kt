package com.example.fitness.ui.meal_detail

import android.os.Build
import android.view.LayoutInflater
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.fitness.data.model.Meal
import com.example.fitness.data.model.MyNutrition
import com.example.fitness.data.model.WorkoutPlan
import com.example.fitness.databinding.ActivityMealDetailBinding
import com.example.fitness.util.base.BaseActivity
import com.example.fitness.util.ext.loadImage
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.UUID

class MealDetailActivity : BaseActivity<ActivityMealDetailBinding, MealDetailViewModel>() {

    private var meal : Meal? = null
    override val viewModel: MealDetailViewModel
            by viewModel()

    override val bindingInflater: (LayoutInflater) -> ActivityMealDetailBinding
        get() = ActivityMealDetailBinding::inflate

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun setupViews() {
        meal = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra("meal", Meal::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra("meal") as? Meal
        }
        setUpData(meal)
    }

    private fun setUpData(meal: Meal?) {
        binding.ivMealDetail.loadImage(meal?.image)
        binding.tvTitleMealDetail.text = meal?.title
        binding.tvKcal.text = meal?.calo
        binding.tvProtein.text = meal?.protein
        binding.tvCarbs.text = meal?.carb
        binding.tvFat.text = meal?.fat
        binding.tvDescriptionMealDetail.text = meal?.description
        binding.tvIronMealDetail.text = meal?.iron
        binding.tvKaliMealDetail.text = meal?.kali
        binding.tvCanxiMealDetail.text = meal?.canxi
        binding.tvMagieMealDetail.text = meal?.magie
        binding.tvZincMealDetail.text = meal?.zinc
        binding.tvCholesterolMealDetail.text = meal?.cholesterol
    }

    override fun setupOnClick() {
        binding.ivBackMealDetail.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
        binding.tvAddMealPlan.setOnClickListener { viewModel.addMyNutrition(MyNutrition(
            id = UUID.randomUUID().toString(),
            meal_id = meal?.id.toString(),
            sport_id = "",
            user_id = FirebaseAuth.getInstance().currentUser?.uid.toString(),
            1
        )) }
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