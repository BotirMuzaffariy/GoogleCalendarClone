package uz.lazycoder.googlecalendarclone

import java.util.*
import android.app.Application
import java.text.SimpleDateFormat
import kotlin.collections.ArrayList
import androidx.lifecycle.MutableLiveData
import uz.lazycoder.googlecalendarclone.utils.Utils
import uz.lazycoder.googlecalendarclone.utils.Consts
import uz.lazycoder.googlecalendarclone.models.MonthM
import com.prolificinteractive.materialcalendarview.CalendarDay

class App : Application() {

    private lateinit var thread: Thread
    private val list = ArrayList<MonthM>()
    private lateinit var runnable: Runnable

    companion object {
        val dateList = MutableLiveData<List<MonthM>>()
    }

    override fun onCreate() {
        super.onCreate()

        runnable = Runnable {
            for (year in Consts.firstYear()..Consts.lastYear()) {
                for (month in 1..12) {
                    findDatesInMonth(year, month)
                }
            }
        }
        thread = Thread(runnable)

        thread.start()
    }

    private fun findDatesInMonth(year: Int, month: Int) {
        val daysList = ArrayList<String>()
        val calendar = Calendar.getInstance()
        val smallSdf = SimpleDateFormat("d-MMM")
        val titleSdf = SimpleDateFormat("MMMM, yyyy")
        val fullSdf = SimpleDateFormat("d-MMM, yyyy")

        calendar.clear()
        calendar.set(year, month - 1, 1)

        val title = titleSdf.format(calendar.time)
        val daysInMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)

        // fill the daysList
        while ((calendar.get(Calendar.DAY_OF_MONTH) < daysInMonth) && (calendar.get(Calendar.MONTH) == month - 1)) {
            // for find monday
            while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.MONDAY) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }

            val monTime = calendar.time
            var days = calendar.get(Calendar.DAY_OF_MONTH).toString()
            val mon = CalendarDay.from(year, month, calendar.get(Calendar.DAY_OF_MONTH))

            // for find sunday
            while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
                calendar.add(Calendar.DAY_OF_MONTH, 1)
            }

            if (mon.year != calendar.get(Calendar.YEAR)) {
                days = "${fullSdf.format(monTime)} ― ${fullSdf.format(calendar.time)}"
            } else {
                if (mon.month != calendar.get(Calendar.MONTH) + 1) {
                    days = "${smallSdf.format(monTime)} ― ${fullSdf.format(calendar.time)}"
                } else {
                    days += " ― ${fullSdf.format(calendar.time)}"
                }
            }

            daysList.add(days)
        }

        list.add(MonthM(Utils.upperFirst(title), daysList, Utils.getMonthImage(month)))
        dateList.postValue(list)
    }

}