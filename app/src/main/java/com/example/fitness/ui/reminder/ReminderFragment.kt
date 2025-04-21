package com.example.fitness.ui.reminder

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import com.example.fitness.data.model.Reminder
import com.example.fitness.databinding.FragmentReminderBinding
import com.example.fitness.ui.add_reminder.AddReminderActivity
import com.example.fitness.util.base.BaseFragment
import com.example.fitness.util.ext.setAdapterLinearVertical
import org.koin.androidx.viewmodel.ext.android.viewModel

class ReminderFragment : BaseFragment<FragmentReminderBinding, ReminderViewModel>() {

    private val adapterReminder = ReminderAdapter(::onClickDelete, ::onClickUpdate)
    override val viewModel: ReminderViewModel
            by viewModel()

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
        viewModel.deleteReminder.observe(viewLifecycleOwner){
            if (it){
                Toast.makeText(requireContext(), "Xóa thành công", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun setUpOnClick() {
        binding.ivAddReminder.setOnClickListener { startActivity(Intent(requireContext(), AddReminderActivity::class.java)) }
    }

    private fun onClickDelete(reminder: Reminder) {
        reminder.id?.let { viewModel.deleteReminder(it) }
    }

    private fun onClickUpdate(reminder: Reminder) {
        startActivity(Intent(requireContext(), AddReminderActivity::class.java).apply {
            putExtra("reminder", reminder)
        })
    }

}