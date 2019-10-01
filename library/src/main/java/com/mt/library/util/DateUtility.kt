package com.mt.library.util

import android.text.format.DateUtils
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateUtility {

    // TimeZone
    val TIMEZONE_GMT = "GMT-7"
    val TIMEZONE_UTC = "UTC"

    object CommonFormat {
        const val D = "d"
        const val EEEE = "EEEE"
    }

    // DateTime Pattern
    object DateTimeFormat {
        const val YYYY_MM_DD_HH_MM_SS_DASH = "yyyy-MM-dd HH:mm:ss"
        const val DD_MM_YYYY_HH_MM_SS_SLASH_DATE = "dd/MM/yyyy HH:mm:ss"
        const val DD_MM_YYYY_HH_MM_SS_DASH_DATE = "dd-MM-yyyy HH:mm:ss"
        const val DD_MM_YYYY_HH_MM_A_SLASH_DATE = "dd/MMM/yyyy HH:mm a"
        const val DD_MM_YYYY_HH_MM_A_DASH_DATE = "dd-MM-yyyy hh:mm a"

    }

    // Date Pattern
    object DateFormat {
        const val YYYY_MM_DD = "yyyy-MM-dd"
        const val DD_MM_YYYY_SLASH = "dd/MM/yyyy"
        const val DD_MM_YYYY_DASH = "dd-MM-yyyy"
        const val MMMM_yyyy = "MMMM yyyy"
        const val EE_MMM_dd = "EEE MMM dd"
        const val MMM_DD_YYYY = "MMM dd, yyyy"
        const val MMMM_dd_yyyy_SLASH = "MMMM/dd/yyyy"
    }

    // Time Pattern
    object TimeFormat {
        const val HH_MM_SS = "HH:mm:ss"
        const val HH_MM = "HH:mm"
        const val hh_mm_a = "hh:mm a"
    }

    private val TAG = DateUtility::class.java.simpleName

    fun format(format: String, date: Date): String {
        return SimpleDateFormat(format, Locale.getDefault()).format(date)
    }

    fun format(format: String, date: String): String {
        return SimpleDateFormat(format, Locale.getDefault()).format(formatDate(date))
    }

    fun format(format: String, milliseconds: Long, locale: Locale?): String {
        if (locale != null)
            return SimpleDateFormat(format, locale).format(Date(milliseconds))

        return SimpleDateFormat(format, Locale.getDefault()).format(Date(milliseconds))
    }

    fun formatDate(dateString: String?): Date? {
        var date: Date? = null
        if (dateString != null) {
            try {
                date =
                    SimpleDateFormat(getDatePattern(dateString), Locale.getDefault()).parse(
                        dateString.trim { it <= ' ' })
            } catch (e: ParseException) {
                e.printStackTrace()
                Log.e(TAG, "formatDate: " + e.message)
            }
        }
        return date
    }

    fun dateToUnixTimestamp(dateString: String): Long {
        try {
            return SimpleDateFormat(getDatePattern(dateString), Locale.getDefault()).parse(dateString).time
        } catch (e: ParseException) {
            e.printStackTrace()
        }

        return -1
    }

    fun unixTimestampToDate(format: String, milliseconds: Long?): String {
        return unixTimestampToDate(format, milliseconds, Locale.getDefault())
    }

    fun unixTimestampToDate(format: String, milliseconds: Long?, locale: Locale): String {
        return SimpleDateFormat(format, locale).format(Date(milliseconds!! * 1000L))
    }

    private fun getDatePattern(dateString: String?): String {
        var pattern = ""
        if (isDateTime(dateString)) {
            if (dateString!!.contains("/")) {
                if (checkIsValid(dateString, DateFormat.DD_MM_YYYY_DASH)) {
                    pattern = DateFormat.DD_MM_YYYY_DASH
                }
            } else {
                if (checkIsValid(DateTimeFormat.DD_MM_YYYY_HH_MM_SS_DASH_DATE, dateString)) {
                    pattern = DateTimeFormat.DD_MM_YYYY_HH_MM_SS_DASH_DATE
                } else if (checkIsValid(DateTimeFormat.YYYY_MM_DD_HH_MM_SS_DASH, dateString)) {
                    pattern = DateTimeFormat.YYYY_MM_DD_HH_MM_SS_DASH
                }
            }
        } else if (dateString!!.contains(":")) {
            when {
                checkIsValid(TimeFormat.HH_MM, dateString) -> pattern = TimeFormat.HH_MM
                checkIsValid(TimeFormat.HH_MM_SS, dateString) -> pattern = TimeFormat.HH_MM_SS
                checkIsValid(TimeFormat.hh_mm_a, dateString) -> pattern = TimeFormat.hh_mm_a
            }
        } else {
            if (dateString.contains("-")) {
                if (checkIsValid(DateFormat.DD_MM_YYYY_DASH, dateString)) {
                    pattern = DateFormat.DD_MM_YYYY_DASH
                }
            } else {
                if (checkIsValid(DateFormat.YYYY_MM_DD, dateString)) {
                    pattern = DateFormat.YYYY_MM_DD
                } else if (checkIsValid(DateFormat.DD_MM_YYYY_SLASH, dateString)) {
                    pattern = DateFormat.DD_MM_YYYY_SLASH
                }
            }
        }
        return pattern
    }

    private fun checkIsValid(format: String, dateString: String): Boolean {
        var isValid: Boolean
        val sdfmt = SimpleDateFormat(format, Locale.getDefault())
        sdfmt.isLenient = false
        try {
            sdfmt.parse(dateString)
            isValid = true
        } catch (e: Exception) {
            isValid = false
        }
        return isValid
    }

    private fun isDateTime(dateString: String?): Boolean {
        return dateString != null && dateString.trim { it <= ' ' }.split(" ".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray().size > 1
    }

    fun getPreviousDateInLong(daysFromToday: Int): Long {
        return System.currentTimeMillis() - daysFromToday * 24 * 60 * 60 * 1000

    }

    fun getNextDateInLong(daysFromToday: Int): Long {
        return System.currentTimeMillis() - daysFromToday * 24 * 60 * 60 * 1000

    }

    fun getCurrentDateTimeInMilliSeconds(): Long {
        return System.currentTimeMillis()
    }

    fun isToday(milliseconds: Long): Boolean {
        return DateUtils.isToday(milliseconds)
    }

    fun getCurrentDateTime(format: String): String? {
        return SimpleDateFormat(format, Locale.getDefault()).format(Date())
    }

    fun addTimeToDate(date: Date, hours: Int, minutes: Int, seconds: Int): Date? {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.HOUR, hours)
        calendar.add(Calendar.MINUTE, minutes)
        calendar.add(Calendar.SECOND, seconds)
        return calendar.time
    }

    fun addHoursToDate(date: Date, hours: Int): Date? {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.HOUR, hours)
        return calendar.time
    }

//    fun convertTimezone(date: Date, timeZone: String): String {
//        val dateFormat = SimpleDateFormat(DATE_TIME_PATTERN_1)
//        dateFormat.timeZone = TimeZone.getTimeZone(timeZone)
//        return dateFormat.format(date)
//    }

//    fun convertTimezone(date: Date?, timeZone: String?): String? {
//        val dateFormat = SimpleDateFormat(
//            DateTimeFormat.DATETIME_PATTERN_YYYY_MM_DD_HH_MM_SS,
//            Locale.getDefault()
//        )
//        dateFormat.timeZone = TimeZone.getTimeZone(timeZone)
//        return dateFormat.format(date)
//    }

    fun convertStringToDate(date: String?, format: String?): Date? {
        val sdfInput = SimpleDateFormat(format, Locale.getDefault())
        return sdfInput.parse(date)
    }

//    fun convertTimezone(date: Date?, outputFormat: String?, timeZone: String?): String? {
//        val sdfOutput = SimpleDateFormat(outputFormat, Locale.getDefault())
////        sdfOutput.timeZone = TimeZone.getTimeZone(timeZone)
//        return sdfOutput.format(date)
//    }
}
