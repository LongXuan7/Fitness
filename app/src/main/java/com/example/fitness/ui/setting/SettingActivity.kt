package com.example.fitness.ui.setting

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.fitness.R
import com.example.fitness.databinding.ActivitySettingBinding
import com.example.fitness.ui.main.MainActivity
import com.example.fitness.util.base.BaseActivity
import com.example.fitness.util.ext.show
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.Locale

class SettingActivity : BaseActivity<ActivitySettingBinding, SettingViewModel>() {
    override val viewModel: SettingViewModel
            by viewModel()


    override val bindingInflater: (LayoutInflater) -> ActivitySettingBinding
        get() = ActivitySettingBinding::inflate

    override fun setupViews() {
        val sharedPref = getSharedPreferences("settings", MODE_PRIVATE)
        val language = sharedPref.getString("language", "vi") ?: "vi"

        if (language == "en") {
            binding.radioButton2.isChecked = true
            binding.textView31.text = "EN"
        } else {
            binding.radioButton.isChecked = true
            binding.textView31.text = "VI"
        }
    }

    override fun setupOnClick() {
        binding.radioButton2.setOnClickListener {
            setLocale("en", "GB")
        }
        binding.radioButton.setOnClickListener {
            setLocale("vi", "VN")
        }
    }

    override fun setupObservers() {

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
        recreate()
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}