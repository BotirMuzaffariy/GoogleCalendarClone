package uz.lazycoder.googlecalendarclone

import java.time.*
import java.util.*
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.*
import androidx.core.view.GravityCompat
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import uz.lazycoder.googlecalendarclone.utils.Utils
import com.prolificinteractive.materialcalendarview.CalendarDay
import uz.lazycoder.googlecalendarclone.adapters.RvCalendarAdapter
import uz.lazycoder.googlecalendarclone.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var topPos = 0
    private lateinit var binding: ActivityMainBinding
    private lateinit var scrollListener: RecyclerView.OnScrollListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = RvCalendarAdapter()
        val layoutManager = binding.rvCalendar.layoutManager as LinearLayoutManager

        scrollListener = object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val currentPos = layoutManager.findFirstVisibleItemPosition()
                if (currentPos != topPos) {
                    val newDay = Utils.getCalendarDay(currentPos)
                    binding.tvDatePickerText.text = Utils.getTitleText(newDay)
                    topPos = currentPos
                }
            }
        }

        binding.apply {
            nv.setCheckedItem(R.id.m_item1)
            calendarView.topbarVisible = false
            calendarView.isDynamicHeightEnabled = true
            calendarView.selectedDate = CalendarDay.today()

            App.dateList.observe(this@MainActivity, {
                adapter.submitList(it)
                topPos = layoutManager.findFirstVisibleItemPosition()
            })

            rvCalendar.adapter = adapter
            rvCalendar.addOnScrollListener(scrollListener)
            tvDatePickerText.text = Utils.getTitleText(CalendarDay.today())
            layoutManager.scrollToPositionWithOffset(Utils.getPosition(CalendarDay.today().year, CalendarDay.today().month), 0)

            calendarView.setOnDateChangedListener { _, date, _ ->
                tvDatePickerText.text = Utils.getTitleText(date)
                layoutManager.scrollToPositionWithOffset(Utils.getPosition(date.year, date.month), 0)
            }

            calendarView.setOnMonthChangedListener { _, date ->
                calendarView.selectedDate = date
                tvDatePickerText.text = Utils.getTitleText(date)
                layoutManager.scrollToPositionWithOffset(Utils.getPosition(date.year, date.month), 0)
            }

            llDatePicker.setOnClickListener {
                if (calendarView.visibility == View.GONE) {
                    openCalendarView()
                } else {
                    closeCalendarView()
                }
            }

            ivCurrentDate.setOnClickListener {
                rvCalendar.stopScroll()
                calendarView.currentDate = CalendarDay.today()
                calendarView.selectedDate = CalendarDay.today()
                tvDatePickerText.text = Utils.getTitleText(CalendarDay.today())
                layoutManager.scrollToPositionWithOffset(Utils.getPosition(CalendarDay.today().year, CalendarDay.today().month), 0)
            }

            ivMenu.setOnClickListener { binding.drawerLayout.openDrawer(GravityCompat.START) }
        }

    }

    private fun openCalendarView() {
        val animation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.translate_to_bottom)

        binding.apply {
            ivDatePickerArrow.animate().rotation(-180f).start()
            calendarView.startAnimation(animation)
            calendarView.visibility = View.VISIBLE

            rvCalendar.suppressLayout(true)
            rvCalendar.removeOnScrollListener(scrollListener)

            val topDay = Utils.getCalendarDay(topPos)
            val currentSelectedDay = calendarView.currentDate
            if (topDay != currentSelectedDay) calendarView.currentDate = topDay
        }
    }

    private fun closeCalendarView() {
        val animation = AnimationUtils.loadAnimation(this@MainActivity, R.anim.translate_to_top)

        binding.apply {
            ivDatePickerArrow.animate().rotation(0f).start()
            calendarView.startAnimation(animation)
            calendarView.visibility = View.GONE

            rvCalendar.suppressLayout(false)
            rvCalendar.addOnScrollListener(scrollListener)
        }
    }

    override fun onBackPressed() {
        when {
            binding.drawerLayout.isDrawerOpen(GravityCompat.START) -> binding.drawerLayout.closeDrawer(GravityCompat.START)
            binding.calendarView.visibility == View.VISIBLE -> closeCalendarView()
            else -> super.onBackPressed()
        }
    }

}