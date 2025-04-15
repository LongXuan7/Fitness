package com.example.fitness.ui.reminder

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.fitness.data.model.Reminder
import com.example.fitness.databinding.FragmentReminderBinding
import com.example.fitness.ui.add_reminder.AddReminderActivity
import com.example.fitness.util.base.BaseFragment
import com.example.fitness.util.ext.setAdapterLinearVertical

class ReminderFragment : BaseFragment<FragmentReminderBinding, ReminderViewModel>() {

    private val adapterReminder = ReminderAdapter(::onClickDelete, ::onClickUpdate)

    override fun inflateBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentReminderBinding {
        return FragmentReminderBinding.inflate(inflater, container, false)
    }

    override fun setUpViews() {
        binding.rcvReminder.setAdapterLinearVertical(adapterReminder)
    }

    override fun setupObservers() {
        viewModel.reminderList.observe(viewLifecycleOwner){
            adapterReminder.submitList(it)
        }
    }

    override fun setUpOnClick() {
        binding.ivAddReminder.setOnClickListener { startActivity(Intent(requireContext(), AddReminderActivity::class.java)) }
    }

    private fun onClickDelete(reminder: Reminder) {

    }

    private fun onClickUpdate(reminder: Reminder) {

    }

}