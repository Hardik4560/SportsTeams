package com.hardik.zujudigital.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

class DateUtils {

    companion object {
        fun dateToTime(date: String): String {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val dateTime = LocalTime.parse(date, formatter)

            val timeString = dateTime.toString() // this will be "18:00:00"
            return timeString
        }

        fun dateToCalendar(date: String): Calendar {
            val dateFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            dateFormat.timeZone = TimeZone.getTimeZone("UTC")
            val date = dateFormat.parse(date)
            val calendar = Calendar.getInstance()
            calendar.time = date
            return calendar
        }
    }
}