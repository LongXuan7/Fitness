package com.example.fitness.ui.home

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.fitness.R
import com.example.fitness.databinding.FragmentHomeBinding
import com.example.fitness.ui.achievement.AchievementActivity
import com.example.fitness.util.OnMainFragmentListener
import com.example.fitness.util.base.BaseFragment

class HomeFragment : BaseFragment<FragmentHomeBinding, HomeViewModel>() {

    private var listener: OnMainFragmentListener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnMainFragmentListener) {
            listener = context
        }
    }

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater, container, false)
    }

    override fun setUpViews() {}

    override fun setupObservers() {}

    override fun setUpOnClick() {
        binding.btnFood.setOnClickListener {
            listener?.onNavigateToMenu(R.id.menu_food)
        }
        binding.btnExercise.setOnClickListener {
            listener?.onNavigateToMenu(R.id.menu_exercise_plan)
        }
        binding.btnAchievement.setOnClickListener {
            startActivity(Intent(requireContext(), AchievementActivity::class.java))
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }
}