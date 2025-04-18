package com.example.fitness.ui.add_reminder

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.app.TimePickerDialog
import android.content.Intent
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewTreeObserver
import android.view.ViewTreeObserver.OnGlobalLayoutListener
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.fitness.data.model.Reminder
import com.example.fitness.data.model.Week
import com.example.fitness.databinding.ActivityAddReminderBinding
import com.example.fitness.ui.meal_plan.WeekMealPlanAdapter
import com.example.fitness.util.FormatTime.formatToDayOfWeekEnglish
import com.example.fitness.util.FormatTime.formatToHourMinute
import com.example.fitness.util.FormatTime.formatToMonthVietnamese
import com.example.fitness.util.FormatTime.formatToYear
import com.example.fitness.util.FormatTime.mergeDateAndTime
import com.example.fitness.util.base.BaseActivity
import com.example.fitness.util.broadcast.ReminderReceiver
import com.example.fitness.util.ext.hide
import com.example.fitness.util.ext.setAdapterLinearHorizontal
import com.example.fitness.util.ext.show
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Calendar
import java.util.Locale
import java.util.UUID

class AddReminderActivity : BaseActivity<ActivityAddReminderBinding, AddReminderViewModel>() {

    private var adapterDayOfWeek: WeekMealPlanAdapter? = null
    private var currentCalendar: Calendar = Calendar.getInstance()
    private var selectedDate = LocalDate.now()
    private var mReminder: Reminder? = null
    override val viewModel: AddReminderViewModel
            by viewModel()

    override val bindingInflater: (LayoutInflater) -> ActivityAddReminderBinding
        get() = ActivityAddReminderBinding::inflate

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    @SuppressLint("SetTextI18n")
    override fun setupViews() {
        fixKeyboardOverlap()
        mReminder = intent.getSerializableExtra("reminder", Reminder::class.java)

        if (mReminder != null) {
            binding.tvMonth.text = formatToMonthVietnamese(mReminder?.time ?: "")
            binding.tvDateOfWeek.text = formatToDayOfWeekEnglish(mReminder?.time ?: "")
            binding.tvYear.text = formatToYear(mReminder?.time ?: "")
            binding.tvAddSave.text = "LƯU"
            binding.tvTitleExercise.text = "CHI TIẾT LỜI NHẮC"
            binding.etNote.setText(mReminder?.title)
            binding.tvTime.text = formatToHourMinute(mReminder?.time ?: "")
            binding.switch1.isChecked = mReminder?.status ?: false
            binding.tvOnOff.text =
                if (mReminder?.status == true) "Bật thông báo" else "Tắt thông báo"
            adapterDayOfWeek = WeekMealPlanAdapter(
                ::onItemClickWeek,
                getTodayIndexUpdate(getWeekDaysFromTime(mReminder?.time ?: ""))
            )
            if (isTodayBefore(mReminder?.time ?: "")) {
                enableButton(false)
            } else {
                enableButton(true)
            }
        } else {
            binding.tvMonth.text = "Tháng ${LocalDate.now().monthValue}"
            binding.tvDateOfWeek.text = LocalDate.now().dayOfWeek.toString()
            binding.tvYear.text = LocalDate.now().year.toString()
            adapterDayOfWeek =
                WeekMealPlanAdapter(::onItemClickWeek, getTodayIndex(getCurrentWeekDays()))
            enableButton(true)
        }

        setUpRecyclerView()
    }

    private fun isTodayBefore(inputTime: String): Boolean {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
        val inputDate = inputFormat.parse(inputTime)

        val today = Calendar.getInstance()
        val inputCalendar = Calendar.getInstance()
        inputCalendar.time = inputDate!!

        today.set(Calendar.HOUR_OF_DAY, 0)
        today.set(Calendar.MINUTE, 0)
        today.set(Calendar.SECOND, 0)
        today.set(Calendar.MILLISECOND, 0)

        inputCalendar.set(Calendar.HOUR_OF_DAY, 0)
        inputCalendar.set(Calendar.MINUTE, 0)
        inputCalendar.set(Calendar.SECOND, 0)
        inputCalendar.set(Calendar.MILLISECOND, 0)

        return inputCalendar.before(today)
    }

    private fun setUpRecyclerView() {
        binding.rcvWeek.apply {
            adapterDayOfWeek?.let { setAdapterLinearHorizontal(it) }

            if (mReminder != null) {
                adapterDayOfWeek?.submitList(getWeekDays(mReminder?.time ?: ""))
            } else {
                adapterDayOfWeek?.submitList(getCurrentWeekDays())
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    override fun setupOnClick() {
        binding.btnNextTime.setOnClickListener { nextWeek() }
        binding.btnBackTime.setOnClickListener { backWeek() }
        binding.llSelectTime.setOnClickListener { selectTime() }
        binding.switch1.setOnCheckedChangeListener { _, isChecked -> updateStatusSwitch(isChecked) }
        binding.tvAddSave.setOnClickListener { if (mReminder != null) updateReminder() else addReminder() }
        binding.ivBackRe.setOnClickListener { onBackPressedDispatcher.onBackPressed() }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun updateReminder() {
        if (binding.etNote.text.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập ghi chú", Toast.LENGTH_SHORT).show()
        } else {
            val reminder = Reminder(
                mReminder?.id.toString(),
                binding.etNote.text.toString(),
                mergeDateAndTime(selectedDate.toString(), binding.tvTime.text.toString()),
                mReminder?.user_id.toString(),
                binding.switch1.isChecked
            )
            viewModel.updateReminder(reminder)
            if (binding.switch1.isChecked) {
                scheduleReminder(reminder)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    private fun addReminder() {
        if (binding.etNote.text.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập ghi chú", Toast.LENGTH_SHORT).show()
        } else {
            val reminder = Reminder(
                UUID.randomUUID().toString(),
                binding.etNote.text.toString(),
                mergeDateAndTime(selectedDate.toString(), binding.tvTime.text.toString()),
                FirebaseAuth.getInstance().currentUser?.uid.toString(),
                binding.switch1.isChecked
            )
            viewModel.addReminder(reminder)

            if (binding.switch1.isChecked) {
                scheduleReminder(reminder)
            }
        }
    }

    private fun updateStatusSwitch(isChecked: Boolean) {
        val status = if (isChecked) "Bật thông báo" else "Tắt thông báo"
        binding.tvOnOff.text = status
    }

    private fun selectTime() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            this,
            { _, selectedHour, selectedMinute ->
                val cal = Calendar.getInstance()
                cal.set(Calendar.HOUR_OF_DAY, selectedHour)
                cal.set(Calendar.MINUTE, selectedMinute)
                val formatter = java.text.SimpleDateFormat("hh:mm a", java.util.Locale.ENGLISH)
                val formattedTime = formatter.format(cal.time)
                binding.tvTime.text = formattedTime
            },
            hour,
            minute,
            false
        )

        timePickerDialog.show()
    }

    override fun setupObservers() {
        viewModel.addReminder.observe(this) {
            if (it) {
                Toast.makeText(this, "Thêm thành công", Toast.LENGTH_SHORT).show()
                onBackPressedDispatcher.onBackPressed()
            }
        }
        viewModel.updateReminder.observe(this) {
            if (it) {
                Toast.makeText(this, "Cập nhật thành công", Toast.LENGTH_SHORT).show()
                onBackPressedDispatcher.onBackPressed()
            }
        }
    }

    private fun isSameMonth(): Boolean {
        val currentMonth = currentCalendar.get(Calendar.MONTH)
        val currentYear = currentCalendar.get(Calendar.YEAR)
        val currentMonthNow = Calendar.getInstance().get(Calendar.MONTH)
        val currentYearNow = Calendar.getInstance().get(Calendar.YEAR)
        return currentMonth == currentMonthNow && currentYear == currentYearNow
    }

    private fun backWeek() {
        val tempCalendar = currentCalendar.clone() as Calendar
        tempCalendar.add(Calendar.WEEK_OF_YEAR, -1)

        val firstDayOfMonth = tempCalendar.clone() as Calendar
        firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1)

        if (tempCalendar.get(Calendar.DAY_OF_MONTH) > 7 &&
            tempCalendar.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH)
        ) {
            currentCalendar.add(Calendar.WEEK_OF_YEAR, -1)
            updateWeekList()
        } else {
            currentCalendar.set(Calendar.DAY_OF_MONTH, 1)
            updateWeekList()
        }
    }

    private fun nextWeek() {
        currentCalendar.add(Calendar.WEEK_OF_YEAR, 1)
        if (isSameMonth()) {
            updateWeekList()
        } else {
            Toast.makeText(
                this,
                "Không thể chuyển sang tuần sau trong tháng này",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun updateWeekList() {
        val weekDays = getWeekDays()
        adapterDayOfWeek?.setSelectedPosition(-1)
        adapterDayOfWeek?.submitList(weekDays)
    }

    private fun getWeekDays(time: String): List<Week> {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)
        val calendar = Calendar.getInstance()

        calendar.time = inputFormat.parse(time)!!
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        val list = mutableListOf<Week>()
        val thuMap = mapOf(
            Calendar.MONDAY to "Mon",
            Calendar.TUESDAY to "Tue",
            Calendar.WEDNESDAY to "Wed",
            Calendar.THURSDAY to "Thu",
            Calendar.FRIDAY to "Fri",
            Calendar.SATURDAY to "Sat",
            Calendar.SUNDAY to "Sun"
        )

        for (i in 0 until 7) {
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            val dateName = thuMap[dayOfWeek] ?: "?"
            val date = calendar.get(Calendar.DAY_OF_MONTH).toString()
            list.add(Week(id = UUID.randomUUID().toString(), dayOfWeek = dateName, date = date))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return list
    }


    private fun getWeekDays(): List<Week> {
        val tempCalendar = currentCalendar.clone() as Calendar
        val currentMonth = tempCalendar.get(Calendar.MONTH)
        val list = mutableListOf<Week>()
        val thuMap = mapOf(
            Calendar.MONDAY to "Mon",
            Calendar.TUESDAY to "Tus",
            Calendar.WEDNESDAY to "Wen",
            Calendar.THURSDAY to "Thu",
            Calendar.FRIDAY to "Fri",
            Calendar.SATURDAY to "Sat",
            Calendar.SUNDAY to "Sun"
        )

        if (tempCalendar.get(Calendar.DAY_OF_MONTH) <= 7) {
            tempCalendar.set(Calendar.DAY_OF_MONTH, 1)
        } else {
            tempCalendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
        }

        for (i in 0 until 7) {
            val month = tempCalendar.get(Calendar.MONTH)
            if (month == currentMonth) {
                val dayOfWeek = tempCalendar.get(Calendar.DAY_OF_WEEK)
                val dateName = thuMap[dayOfWeek] ?: "?"
                val date = tempCalendar.get(Calendar.DAY_OF_MONTH).toString()
                list.add(Week(id = UUID.randomUUID().toString(), dayOfWeek = dateName, date = date))
            }
            tempCalendar.add(Calendar.DAY_OF_MONTH, 1)
        }
        return list
    }

    private fun getWeekDaysFromTime(time: String): List<Week> {
        val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.ENGLISH)
        val calendar = Calendar.getInstance()

        calendar.time = inputFormat.parse(time)!!
        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        val list = mutableListOf<Week>()
        val thuMap = mapOf(
            Calendar.MONDAY to "Mon",
            Calendar.TUESDAY to "Tue",
            Calendar.WEDNESDAY to "Wed",
            Calendar.THURSDAY to "Thu",
            Calendar.FRIDAY to "Fri",
            Calendar.SATURDAY to "Sat",
            Calendar.SUNDAY to "Sun"
        )

        for (i in 0 until 7) {
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            val dateName = thuMap[dayOfWeek] ?: "?"
            val date = calendar.get(Calendar.DAY_OF_MONTH).toString()
            list.add(
                Week(
                    id = UUID.randomUUID().toString(),
                    dayOfWeek = dateName,
                    date = date
                )
            )
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return list
    }


    private fun getCurrentWeekDays(): List<Week> {
        val calendar = Calendar.getInstance()

        calendar.firstDayOfWeek = Calendar.MONDAY
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)

        val list = mutableListOf<Week>()
        val thuMap = mapOf(
            Calendar.MONDAY to "Mon",
            Calendar.TUESDAY to "Tus",
            Calendar.WEDNESDAY to "Wen",
            Calendar.THURSDAY to "Thu",
            Calendar.FRIDAY to "Fri",
            Calendar.SATURDAY to "Sat",
            Calendar.SUNDAY to "Sun"
        )

        for (i in 0 until 7) {
            val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
            val dateName = thuMap[dayOfWeek] ?: "?"
            val date = calendar.get(Calendar.DAY_OF_MONTH).toString()
            list.add(Week(id = UUID.randomUUID().toString(), dayOfWeek = dateName, date = date))
            calendar.add(Calendar.DAY_OF_MONTH, 1)
        }

        return list
    }

    private fun getTodayIndex(weekList: List<Week>): Int {
        val today = Calendar.getInstance()
        val todayDate = today.get(Calendar.DAY_OF_MONTH).toString()
        return weekList.indexOfFirst { it.date == todayDate }
    }

    private fun getTodayIndexUpdate(weekList: List<Week>): Int {
        return weekList.indexOfFirst { it.date == mReminder?.time?.substring(8, 10) }
    }

    private fun onItemClickWeek(week: Week) {
        val date = week.date.toInt()
        val month = binding.tvMonth.text.toString().replace("Tháng ", "").toInt()
        val year = binding.tvYear.text.toString().toInt()
        selectedDate = LocalDate.of(year, month, date)
        binding.tvDateOfWeek.text = selectedDate.dayOfWeek.toString()

        val itemDate = LocalDate.of(year, month, date)
        val currentDate = LocalDate.now()

        if (!itemDate.isBefore(currentDate)) {
            binding.tvAddSave.show()
        } else {
            binding.tvAddSave.hide()
        }
    }

    private fun enableButton(isChecked: Boolean) {
        if (isChecked) {
            binding.tvAddSave.show()
        } else {
            binding.tvAddSave.hide()
        }
        binding.btnNextTime.isEnabled = isChecked
        binding.btnBackTime.isEnabled = isChecked
        binding.llSelectTime.isEnabled = isChecked
        binding.switch1.isEnabled = isChecked
        binding.etNote.isEnabled = isChecked
        adapterDayOfWeek?.setItemClickable(isChecked)
    }

    private fun fixKeyboardOverlap() {
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            private var isKeyboardVisible = false

            override fun onGlobalLayout() {
                val rect = Rect()
                binding.root.getWindowVisibleDisplayFrame(rect)

                val screenHeight = binding.root.rootView.height
                val keypadHeight = screenHeight - rect.bottom

                val shouldShow = keypadHeight > screenHeight * 0.15

                if (shouldShow != isKeyboardVisible) {
                    isKeyboardVisible = shouldShow

                    val targetTranslation = if (shouldShow) {
                        -keypadHeight / 1.8f
                    } else {
                        0f
                    }

                    ObjectAnimator.ofFloat(
                        binding.etNote,
                        "translationY",
                        binding.etNote.translationY,
                        targetTranslation
                    ).apply {
                        duration = 200  // 200 milliseconds
                        interpolator = AccelerateDecelerateInterpolator()
                        start()
                    }
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.S)
    @SuppressLint("ScheduleExactAlarm")
    private fun scheduleReminder(reminder: Reminder) {
        val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager

        if (!alarmManager.canScheduleExactAlarms()) {
            AlertDialog.Builder(this)
                .setTitle("Cấp quyền nhắc nhở chính xác")
                .setMessage("Để nhắc bạn đúng giờ, ứng dụng cần quyền đặt báo chính xác. Vui lòng cấp quyền này trong cài đặt.")
                .setPositiveButton("Mở cài đặt") { _, _ ->
                    val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                        data = Uri.parse("package:$packageName")
                    }
                    startActivity(intent)
                }
                .setNegativeButton("Hủy", null)
                .show()
            return
        }

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            reminder.id.hashCode(), // Dùng requestCode để nhận diện alarm cũ
            Intent(this, ReminderReceiver::class.java).apply {
                putExtra("title", reminder.title)
                putExtra("time", reminder.time)
            },
            PendingIntent.FLAG_NO_CREATE or PendingIntent.FLAG_IMMUTABLE // Để chỉ tìm PendingIntent cũ mà không tạo mới
        )

        pendingIntent?.let {
            alarmManager.cancel(it)
        }


        val intent = Intent(this, ReminderReceiver::class.java).apply {
            putExtra("title", reminder.title)
            putExtra("time", reminder.time)
        }

        val newPendingIntent = PendingIntent.getBroadcast(
            this,
            reminder.id.hashCode(), // Dùng lại requestCode
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val formatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault())
        val timeInMillis = reminder.time?.let { formatter.parse(it)?.time } ?: return


        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            timeInMillis,
            newPendingIntent
        )
    }

}