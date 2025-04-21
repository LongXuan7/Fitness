package com.example.fitness.util.base

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.example.fitness.util.dialogManager.DialogManager
import com.example.fitness.util.dialogManager.DialogManagerImpl


abstract class BaseActivity<VB : ViewBinding, VM : ViewModel> : AppCompatActivity() {

    protected lateinit var binding: VB
    protected abstract val viewModel: VM
    private var dialogManager: DialogManager? = null
    protected abstract val bindingInflater: (LayoutInflater) -> VB

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = bindingInflater.invoke(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(binding.root) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(0, systemBars.top, 0, 0)
            WindowInsetsCompat.CONSUMED
        }


        dialogManager = DialogManagerImpl(this)

        setupViews()
        setupOnClick()
        setupObservers()
    }

    abstract fun setupViews()
    abstract fun setupOnClick()
    abstract fun setupObservers()

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

    open fun registerLiveData() {
        if (viewModel is BaseViewModel) {
            (viewModel as BaseViewModel).isLoading.observe(this) {
                if (it) showLoading() else hideLoading()
            }
        }
    }
}
