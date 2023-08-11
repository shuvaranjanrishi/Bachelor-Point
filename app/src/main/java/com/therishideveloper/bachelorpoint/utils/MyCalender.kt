package com.therishideveloper.bachelorpoint.utils

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.os.Build
import android.util.Log
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import com.therishideveloper.bachelorpoint.listener.MyDate
import com.therishideveloper.bachelorpoint.listener.MyDateAndDay
import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

object MyCalender {

    private val myCalendar = Calendar.getInstance()
    private val simpleDateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.US)

    val currentTimeStamp: String
        get() = SimpleDateFormat("dd-MM-yyyy HH:mm:ss", Locale.US).format(Date())
    val currentHourMinSec: String
        get() = SimpleDateFormat("HH:mm:ss", Locale.US).format(Date())
    val dayToday: String
        get() = android.text.format.DateFormat.format("EEEE", Date()).toString()
    val currentDate: String
        get() = SimpleDateFormat("dd-MM-yyyy", Locale.US).format(Date())
    val currentDayAndDate: String
        get() = android.text.format.DateFormat.format("EEEE", Date())
            .toString() + " " + SimpleDateFormat("dd-MM-yyyy", Locale.US).format(Date())
    val currentMonth: String
        get() = SimpleDateFormat("MMMM", Locale.US).format(myCalendar.time);
    val currentYear: String
        get() = SimpleDateFormat("yyyy", Locale.US).format(Date())
    val currentMonthYear: String
        get() = SimpleDateFormat(
            "MMMM",
            Locale.US
        ).format(myCalendar.time) + " " + SimpleDateFormat("yyyy", Locale.US).format(Date())

    //
//    val calendar = Calendar.getInstance()
////
//@RequiresApi(Build.VERSION_CODES.O)
//val current = LocalDateTime.of(
//        calendar.get(Calendar.YEAR),
//        calendar.get(Calendar.MONTH),
//        calendar.get(Calendar.DAY_OF_MONTH),
//        calendar.get(Calendar.HOUR_OF_DAY),
//        calendar.get(Calendar.MINUTE),
//        calendar.get(Calendar.SECOND)
//    )
    val previousDate: String
        get() {
            val sdf = SimpleDateFormat("dd-MM-yyyy", Locale.US)
            var date: Date? = null
            try {
                date = sdf.parse(currentDate)
            } catch (e: ParseException) {
                e.printStackTrace()
            }
            val calendar = Calendar.getInstance()
            if (date != null) {
                calendar.time = date
            }
            calendar.add(Calendar.DATE, -1)
            return sdf.format(calendar.time)
        }

    fun formatDate(date: String?, dateFormat: String?): String? {
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        var d: Date? = null
        try {
            d = date?.let { sdf.parse(it) }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        sdf.applyPattern(dateFormat)
        return d?.let { sdf.format(it) }
    }

    fun getDateFromSting(date: String?): Date? {
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        var d: Date? = null
        try {
            d = date?.let { sdf.parse(it) }
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return d
    }

    fun getFormattedDate(date: String?): String {
        Log.e("dates", date!!)
        val sdf = SimpleDateFormat("dd-MM-yyyy")
        var d: Date? = null
        try {
            d = sdf.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val cal = Calendar.getInstance()
        cal.time = d
        val dayNumberSuffix = getDayNumberSuffix(cal[Calendar.DAY_OF_MONTH])
        val dateFormat: DateFormat = SimpleDateFormat(" d'$dayNumberSuffix' MMMM yyyy")
        return dateFormat.format(cal.time)
    }

    fun getMsgDate(date: String?): String {
        Log.e("dates", date!!)
        val sdf = SimpleDateFormat("dd-MM-yyyy HH:mm:ss")
        var d: Date? = null
        try {
            d = sdf.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        val cal = Calendar.getInstance()
        cal.time = d
        val dayNumberSuffix = getDayNumberSuffix(cal[Calendar.DAY_OF_MONTH])
        val dateFormat: DateFormat = SimpleDateFormat("d'$dayNumberSuffix' MMMM yyyy KK:mm a")
        return dateFormat.format(cal.time)
    }

    val firstDateOfMonth: String
        @RequiresApi(Build.VERSION_CODES.O)
        get() {
            return LocalDate.now().withDayOfMonth(1)
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        }

    val lastDateOfMonth: String
        @RequiresApi(Build.VERSION_CODES.O)
        get() {
            return LocalDate.now().withDayOfMonth(LocalDate.now().lengthOfMonth())
                .format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))
        }

    private fun getDayNumberSuffix(day: Int): String {
        return if (day >= 11 && day <= 13) {
            "th"
        } else when (day % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }

    fun pickDate(activity: Activity?, dateListener: MyDate) {
        val endDateListener =
            OnDateSetListener { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = monthOfYear
                myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                dateListener.onPickDate(simpleDateFormat.format(myCalendar.time))
            }
        DatePickerDialog(
            activity!!,
            endDateListener,
            myCalendar[Calendar.YEAR],
            myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        ).show()
    }

    fun pickDateAndDay(activity: Activity?, dateListener: MyDateAndDay) {
        val endDateListener =
            OnDateSetListener { view: DatePicker?, year: Int, monthOfYear: Int, dayOfMonth: Int ->
                myCalendar[Calendar.YEAR] = year
                myCalendar[Calendar.MONTH] = monthOfYear
                myCalendar[Calendar.DAY_OF_MONTH] = dayOfMonth
                dateListener.onPickDateAndDay(simpleDateFormat.format(myCalendar.time), dayToday)
            }
        DatePickerDialog(
            activity!!,
            endDateListener,
            myCalendar[Calendar.YEAR],
            myCalendar[Calendar.MONTH],
            myCalendar[Calendar.DAY_OF_MONTH]
        ).show()
    }
}