package com.example.fitness.ui.exercise

import android.content.Intent
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.fitness.data.model.Category
import com.example.fitness.data.model.Exercise
import com.example.fitness.databinding.FragmentExerciseBinding
import com.example.fitness.ui.exercise_detail.ExerciseDetailActivity
import com.example.fitness.util.base.BaseFragment
import com.example.fitness.util.ext.hide
import com.example.fitness.util.ext.setAdapterLinearVertical
import com.example.fitness.util.ext.show
import org.koin.androidx.viewmodel.ext.android.viewModel

class ExerciseFragment : BaseFragment<FragmentExerciseBinding, CategoryViewmodel>() {

    private val adapterCategory = CategoryAdapter(::onItemCategoryClick)
    private val adapterExercise = ExerciseAdapter(::onItemExerciseClick)
    private var mCategory: Category? = null
    override val viewModel : CategoryViewmodel by viewModel()

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentExerciseBinding {
        return FragmentExerciseBinding.inflate(inflater, container, false)
    }

    override fun setUpViews() {
        setUpRecyclerView()
        setUpOnClick()
    }

    override fun setUpOnClick() {
        binding.ivBackExercise.setOnClickListener {
            viewModel.fetchCategories()
            setUpRecyclerView()
            binding.etSearchExercise.text = null
        }

        binding.etSearchExercise.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val query = s.toString()
                val isShow = binding.ivBackExercise.visibility == View.VISIBLE

                if (isShow) {
                    adapterExercise.filter(query)
                } else {
                    adapterCategory.filter(query)
                }
            }

            override fun afterTextChanged(s: Editable?) {}
        })
    }

    private fun setUpRecyclerView() {
        binding.rcvExercise.apply {
            binding.ivBackExercise.hide()
            setAdapterLinearVertical(adapterCategory)
        }
    }

    override fun setupObservers() {
        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            adapterCategory.submitList(categories)
        }

        viewModel.exercise.observe(viewLifecycleOwner) {
            adapterExercise.submitList(it)
        }
    }

    private fun onItemCategoryClick(category: Category) {
        updateData(category)
    }

    private fun updateData(category: Category) {
        mCategory = category
        binding.tvTitleExercise.text = category.name
        viewModel.fetchExercise(category.id)
        setUpRecyclerViewExercise()
        binding.etSearchExercise.text = null
    }

    private fun setUpRecyclerViewExercise() {
        binding.rcvExercise.apply {
            binding.ivBackExercise.show()
            setAdapterLinearVertical(adapterExercise)
        }
    }

    private fun onItemExerciseClick(exercise: Exercise) {
        val intent = Intent(requireContext(), ExerciseDetailActivity::class.java)
        intent.putExtra("exercise", exercise)
        intent.putExtra("title", mCategory?.name)
        startActivity(intent)
    }
}