package com.azmat.testdrivendevelopment.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    fun formatDate(selectedTimeInMillis : Long?) : String {
        if (selectedTimeInMillis == null) {
            Log.d("DateUtils", "null date")
            return "Null date"
        }
        val selected_cal = Calendar.getInstance()
        selected_cal.timeInMillis = selectedTimeInMillis
        val dateFormat = SimpleDateFormat("EEE d MMM")
        val current_cal = Calendar.getInstance()
        val current_year = current_cal.get(Calendar.YEAR)
        val current_month = current_cal.get(Calendar.MONTH)
        val current_date = current_cal.get(Calendar.DAY_OF_MONTH)
        val selected_year = selected_cal.get(Calendar.YEAR)
        val selected_month = selected_cal.get(Calendar.MONTH)
        val selected_date = selected_cal.get(Calendar.DAY_OF_MONTH)

        if (current_year == selected_year) {
            if (current_month == selected_month) {
                if (current_date == selected_date)
                    return "Today"
                if (current_date == selected_date + 1)
                    return "Yesterday"
                if (current_date == selected_date - 1)
                    return "Tomorrow"
            }
            return dateFormat.format(selected_cal.time).toString()
        }

        return dateFormat.format(selected_cal.time).toString() + ", $selected_year"

    }

}