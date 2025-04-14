package com.example.fitness.ui.exercise_detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.fitness.data.model.GuideStep
import com.example.fitness.databinding.LayoutItemGuideBinding
import com.example.fitness.util.base.BaseAdapter

class GuideStepAdapter() : BaseAdapter<GuideStep, LayoutItemGuideBinding>(DIFF_GUIDE_STEP) {

    companion object {
        val DIFF_GUIDE_STEP = object : DiffUtil.ItemCallback<GuideStep>() {
            override fun areItemsTheSame(oldItem: GuideStep, newItem: GuideStep): Boolean {
                return oldItem.title == newItem.title
            }

            override fun areContentsTheSame(oldItem: GuideStep, newItem: GuideStep): Boolean {
                return oldItem == newItem
            }
        }
    }

    override fun createBinding(parent: ViewGroup): LayoutItemGuideBinding {
        val inflater = LayoutInflater.from(parent.context)
        return LayoutItemGuideBinding.inflate(inflater, parent, false)
    }

    @SuppressLint("SetTextI18n")
    override fun bind(binding: LayoutItemGuideBinding, item: GuideStep, position: Int) {
        binding.tvStep.text = (position + 1).toString()
        binding.tvGuideStepTitle.text = item.title
    }
}