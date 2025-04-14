package com.example.fitness.ui.main

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.fitness.R
import com.example.fitness.databinding.ActivityMainBinding
import com.example.fitness.ui.exercise.ExerciseFragment
import com.example.fitness.ui.food.FoodFragment
import com.example.fitness.ui.home.HomeFragment
import com.example.fitness.ui.plan.PlanFragment
import com.example.fitness.ui.profile.ProfileFragment
import com.example.fitness.util.OnMainFragmentListener

class MainActivity : AppCompatActivity(), OnMainFragmentListener {

    private var binding : ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding?.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        transitionToFragment(HomeFragment())
        setUpBottomNavigation()
        checkSwap(intent.getBooleanExtra("FoodFragment", false))
    }

    private fun checkSwap(isCheck : Boolean) {
       if (isCheck)  binding?.bottomNavigationView?.selectedItemId = R.id.menu_food
    }

    private fun setUpBottomNavigation() {
        binding?.bottomNavigationView?.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_home -> {
                    transitionToFragment(HomeFragment())
                    true
                }
                R.id.menu_exercise -> {
                    transitionToFragment(ExerciseFragment())
                    true
                }
                R.id.menu_exercise_plan -> {
                    transitionToFragment(PlanFragment())
                    true
                }
                R.id.menu_food -> {
                    transitionToFragment(FoodFragment())
                    true
                }
                R.id.menu_profile -> {
                    transitionToFragment(ProfileFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun transitionToFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view, fragment)
            .addToBackStack(null)
            .commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }

    override fun onNavigateToMenu(menuId: Int) {
        binding?.bottomNavigationView?.selectedItemId = menuId
    }
}