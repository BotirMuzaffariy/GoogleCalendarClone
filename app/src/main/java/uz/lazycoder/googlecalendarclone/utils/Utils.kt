package uz.lazycoder.googlecalendarclone.utils

import java.util.*
import java.text.SimpleDateFormat
import uz.lazycoder.googlecalendarclone.R
import com.prolificinteractive.materialcalendarview.CalendarDay

object Utils {

    fun getMonthImage(monthNum: Int) = when (monthNum) {
        1 -> R.drawable.i_01_jan
        2 -> R.drawable.i_02_feb
        3 -> R.drawable.i_03_mar
        4 -> R.drawable.i_04_apr
        5 -> R.drawable.i_05_may
        6 -> R.drawable.i_06_jun
        7 -> R.drawable.i_07_jul
        8 -> R.drawable.i_08_aug
        9 -> R.drawable.i_09_sep
        10 -> R.drawable.i_10_oct
        11 -> R.drawable.i_11_nov
        12 -> R.drawable.i_12_dec
        else -> R.drawable.i_01_jan
    }

    fun getPosition(year: Int, month: Int) = ((year - Consts.firstYear()) * 12) + (month - 1)

    fun getCalendarDay(position: Int): CalendarDay {
        return CalendarDay.from(Consts.firstYear() + (position / 12), (position % 12) + 1, 1)
    }

    fun getTitleText(cDay: CalendarDay): String {
        val smallSdf = SimpleDateFormat("MMMM")
        val fullSdf = SimpleDateFormat("MMM, yyyy")

        return if (cDay.year == CalendarDay.today().year) {
            upperFirst(smallSdf.format(getDate(cDay)))
        } else {
            upperFirst(fullSdf.format(getDate(cDay)))
        }
    }

    private fun getDate(cDay: CalendarDay): Date {
        val calendar = Calendar.getInstance()
        calendar.set(cDay.year, cDay.month - 1, cDay.day)
        return calendar.time
    }

    fun upperFirst(str: String) = str.substring(0, 1).uppercase() + str.substring(1)

}