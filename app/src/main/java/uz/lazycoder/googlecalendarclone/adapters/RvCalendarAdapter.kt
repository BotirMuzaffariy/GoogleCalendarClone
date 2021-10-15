package uz.lazycoder.googlecalendarclone.adapters

import android.view.View
import android.view.ViewGroup
import android.view.LayoutInflater
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import uz.lazycoder.googlecalendarclone.models.MonthM
import uz.lazycoder.googlecalendarclone.databinding.ItemCalendarBinding
import com.gjiazhe.scrollparallaximageview.parallaxstyle.VerticalMovingStyle

class RvCalendarAdapter : ListAdapter<MonthM, RvCalendarAdapter.CalendarVh>(MyCalendarDiffUtil()) {

    inner class CalendarVh(private val itemBinding: ItemCalendarBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {
        fun onBind(monthM: MonthM) {
            itemBinding.apply {
                tvTitle.text = monthM.title
                ivMonthImg.setImageResource(monthM.img)
                ivMonthImg.setParallaxStyles(VerticalMovingStyle())

                if (monthM.days.isNotEmpty()) {
                    tvWeek1.text = monthM.days[0]
                    tvWeek2.text = monthM.days[1]
                    tvWeek3.text = monthM.days[2]
                    tvWeek4.text = monthM.days[3]

                    if (monthM.days.size == 5) {
                        tvWeek5.text = monthM.days[4]
                        tvWeek5.visibility = View.VISIBLE
                    } else {
                        tvWeek5.visibility = View.GONE
                    }
                }
            }
        }
    }

    class MyCalendarDiffUtil : DiffUtil.ItemCallback<MonthM>() {
        override fun areItemsTheSame(oldItem: MonthM, newItem: MonthM): Boolean {
            return oldItem.title == newItem.title
        }

        override fun areContentsTheSame(oldItem: MonthM, newItem: MonthM): Boolean {
            return oldItem == newItem
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CalendarVh {
        return CalendarVh(
            ItemCalendarBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CalendarVh, position: Int) {
        holder.onBind(getItem(position))
    }

}