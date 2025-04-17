package com.example.fitness.util.base

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewbinding.ViewBinding
import com.example.fitness.util.dialogManager.DialogManager
import com.example.fitness.util.dialogManager.DialogManagerImpl

abstract class BaseFragment<VB : ViewBinding, VM : BaseViewModel> : Fragment() {

    private var _binding: VB? = null
    protected val binding get() = _binding!!
    protected abstract val viewModel: VM

    private var dialogManager: DialogManager? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dialogManager = DialogManagerImpl(getContext())
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflateBinding(inflater, container)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpViews()
        setUpOnClick()
        setupObservers()
    }

    protected abstract fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): VB

    override fun onStart() {
        super.onStart()
        registerLiveData()
    }

    private fun showLoading() {
        dialogManager?.showLoading()
    }

    private fun hideLoading() {
        dialogManager?.hideLoading()
    }

    abstract fun setUpViews()

    abstract fun setupObservers()

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    open fun registerLiveData() {
        viewModel.isLoading.observe(viewLifecycleOwner) {
            if (it) showLoading() else hideLoading()
        }
    }

    abstract fun setUpOnClick()
}