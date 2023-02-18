package com.hardik.zujudigital.util

import android.os.Build
import androidx.annotation.RequiresApi
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

class DateUtils {

    companion object {
        @RequiresApi(Build.VERSION_CODES.O)
        fun dateToTime(date: String): String {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
            val dateTime = LocalTime.parse(date, formatter)

            val timeString = dateTime.toString() // this will be "18:00:00"
            return timeString
        }
    }
}