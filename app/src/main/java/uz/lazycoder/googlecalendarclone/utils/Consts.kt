package uz.lazycoder.googlecalendarclone.utils

import com.prolificinteractive.materialcalendarview.CalendarDay

object Consts {

    fun firstYear() = CalendarDay.today().year - 100
    fun lastYear() = CalendarDay.today().year + 200

}