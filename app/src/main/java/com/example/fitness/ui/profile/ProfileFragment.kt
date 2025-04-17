package com.example.fitness.ui.profile

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fitness.R
import com.example.fitness.databinding.FragmentProfileBinding
import com.example.fitness.util.base.BaseFragment

class ProfileFragment : BaseFragment<FragmentProfileBinding, ProfileViewModel>() {
    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentProfileBinding {
        return FragmentProfileBinding.inflate(inflater, container, false)
    }

    override fun setUpViews() {

    }

    override fun setupObservers() {

    }

    override fun setUpOnClick() {

    }
}