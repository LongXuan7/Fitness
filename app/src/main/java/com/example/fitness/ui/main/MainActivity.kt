package com.example.fitness.ui.main

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
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
import com.example.fitness.ui.reminder.ReminderFragment
import com.example.fitness.util.OnMainFragmentListener
import com.google.firebase.auth.FirebaseAuth
import java.util.Locale

class MainActivity : AppCompatActivity(), OnMainFragmentListener {

    private var binding: ActivityMainBinding? = null

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
        val sharedPref = getSharedPreferences("settings", MODE_PRIVATE)
        val language = sharedPref.getString("language", "vi") ?: "vi"
        val country = sharedPref.getString("country", "vi") ?: "vi"

        setLocale(language, country)
        transitionToFragment(HomeFragment())
        setUpBottomNavigation()
        checkSwap(intent.getBooleanExtra("FoodFragment", false))
    }

    private fun checkSwap(isCheck: Boolean) {
        if (isCheck) binding?.bottomNavigationView?.selectedItemId = R.id.menu_food
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
        if (menuId == 0) {
            transitionToFragment(ReminderFragment())
        } else {
            binding?.bottomNavigationView?.selectedItemId = menuId
        }
    }

    private fun setLocale(languageCode: String, country: String = "") {
        val locale = Locale(languageCode, country)
        Locale.setDefault(locale)

        val config = Configuration()
        config.setLocale(locale)
        resources.updateConfiguration(config, resources.displayMetrics)

        val sharedPref = getSharedPreferences("settings", MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("language", languageCode)
            putString("country", country)
            apply()
        }
    }
}