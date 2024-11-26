package com.example.bookpointkotlin.model
import java.text.SimpleDateFormat
import java.util.*

class booking_datetime_converted{

    object DateConverted {
        fun getDate(date: Date): String {
            val formatter = SimpleDateFormat("M/d/yyyy", Locale.getDefault())
            return formatter.format(date)
        }

        fun getDay(day: Int): String {
            return when (day) {
                1 -> "Monday"
                2 -> "Tuesday"
                3 -> "Wednesday"
                4 -> "Thursday"
                5 -> "Friday"
                6 -> "Saturday"
                7 -> "Sunday"
                else -> "Sunday"
            }
        }

        fun getTime(time: Int): String {
            return when (time) {
                0 -> "9:00 AM"
                1 -> "10:00 AM"
                2 -> "11:00 AM"
                3 -> "12:00 PM"
                4 -> "1:00 PM"
                5 -> "2:00 PM"
                6 -> "3:00 PM"
                7 -> "4:00 PM"
                else -> "9:00 AM"
            }
        }
    }


}