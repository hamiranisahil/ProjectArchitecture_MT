package com.example.library.util

import android.annotation.SuppressLint
import android.text.format.DateUtils
import android.util.Log
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class DateUtility {
    /*
    EEE, EEEE : Mon, Monday
    MMM, MMMM : Mar , March
     */

    // TimeZone
    val TIMEZONE_GMT = "GMT-7"
    val TIMEZONE_UTC = "UTC"

    object CommonFormat {
        const val D = "d"
        const val M = "m"
        const val MM = "mm"
        const val DD = "dd"
        const val yyyy = "yyyy"
        const val EEEE = "EEEE"
    }

    // DateTime Pattern
    object DateTimeFormat {
        const val YYYY_MM_DD_HH_MM_SS_DASH = "yyyy-MM-dd HH:mm:ss"
        const val DD_MM_YYYY_HH_MM_SS_SLASH_DATE = "dd/MM/yyyy HH:mm:ss"
        const val DD_MM_YYYY_HH_MM_SS_DASH_DATE = "dd-MM-yyyy HH:mm:ss"
        const val DD_MM_YYYY_HH_MM_A_SLASH_DATE = "dd/MMM/yyyy HH:mm a"
        const val DD_MM_YYYY_HH_MM_A_DASH_DATE = "dd-MM-yyyy hh:mm a"
        val MMM_DD_YYYY_HH_MM_A_DASH_DATE = "${DateFormat.MMM_DD_YYYY} ${TimeFormat.hh_mm_a}"

    }
    // Date Pattern
    object DateFormat {
        const val YYYY_MM_DD = "yyyy-MM-dd"
        const val DD_MM_YYYY_SLASH = "dd/MM/yyyy"
        const val DD_MM_YYYY_DASH = "dd-MM-yyyy"
        const val MMMM_yyyy = "MMMM yyyy"
        //        const val EE_MMM_dd = "EEE MMM dd"
        const val MMM_DD_YYYY = "MMM dd, yyyy"
        const val MMMM_DD_YYYY = "MMMM dd, yyyy"
        const val MMMM_dd_yyyy_SLASH = "MMMM/dd/yyyy"
        const val MM_dd_yy_SLASH = "MM/dd/yy"
        const val EEE_MMM_dd = "EEE, MMM, dd"
        const val MMMM_DD = "MMMM dd"
        const val EEEE_MMM_dd = "EEEE, MMM. dd"
        const val MMM_dd_YYYY = "MMM. dd, yyy"
        const val MM_DD_YYYY_SLASH_h_mm_a = "MM/dd/yy, h:mm a"
        val MMM_DD = "MMM dd"
    }

    // Time Pattern
    object TimeFormat {
        const val HH_MM_SS = "HH:mm:ss"
        const val HH_MM = "HH:mm"
        const val hh_mm_a = "hh:mm a"
        const val h_mm_a = "h:mm a"
    }

    private val TAG = DateUtility::class.java.simpleName

    fun format(format: String, date: Date): String {
        return SimpleDateFormat(format, Locale.getDefault()).format(date)
    }

    fun format(format: String, date: String): String {
        return SimpleDateFormat(format, Locale.getDefault()).format(formatDate(format, date))
    }

    fun format(format: String, milliseconds: Long, locale: Locale?): String {
        if (locale != null)
            return SimpleDateFormat(format, locale).format(Date(milliseconds))

        return SimpleDateFormat(format, Locale.getDefault()).format(Date(milliseconds))
    }

    fun formatDate(format: String, dateString: String?): Date? {
        var date: Date? = null
        if (dateString != null) {
            try {
                date =
                    SimpleDateFormat(getDatePattern(dateString, format), Locale.getDefault()).parse(
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

    @SuppressLint("SimpleDateFormat")
    fun convertDate(date: String, input_formate: String, output_formate: String): String {
        try {
            val Input_formate = SimpleDateFormat(input_formate)
            val date1 = Input_formate.parse(date)
            val Output_formate = SimpleDateFormat(output_formate)
            printLog("convertDate", "app= " + Output_formate.format(date1))
            return Output_formate.format(date1)
        } catch (e: Exception) {
            e.printStackTrace()
            printLog("convertDate", "" + e.message)
        }
        return ""

    }

    fun unixTimestampToDate(format: String, milliseconds: Long?): String {
        return unixTimestampToDate(format, milliseconds, Locale.getDefault())
    }

    fun unixTimestampToDate(format: String, milliseconds: Long?, locale: Locale): String {
        return SimpleDateFormat(format, locale).format(Date(milliseconds!! * 1000L))
    }

    private fun getDatePattern(dateString: String?, format: String? = null): String {
        var pattern = ""
        if (format.equals(CommonFormat.D)) {
            pattern = format!!
        } else if (format.equals(CommonFormat.M)) {
            pattern = format!!
        } else if (format.equals(CommonFormat.MM)) {
            pattern = format!!
        } else if (isDateTime(dateString)) {
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
                    pattern = format!!
                } else if (checkIsValid(DateFormat.YYYY_MM_DD, dateString)) {
                    pattern = format!!
                }
            } else {
                when {
                    checkIsValid(DateFormat.YYYY_MM_DD, dateString) -> {
                        pattern = DateFormat.YYYY_MM_DD
                    }
                    checkIsValid(DateFormat.DD_MM_YYYY_SLASH, dateString) -> {
                        pattern = DateFormat.DD_MM_YYYY_SLASH
                    }
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

    fun addHoursToDate(date: Date, hours: Int, minutes: Int, format: String): String? {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.HOUR, hours)
        calendar.add(Calendar.MINUTE, minutes)
        return SimpleDateFormat(format, Locale.getDefault()).format(calendar.time)
    }

    fun addMinutesToDate(date: Date, minutes: Int, format: String): String? {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MINUTE, minutes)
        return SimpleDateFormat(format, Locale.getDefault()).format(calendar.time)
    }


    fun addMinutesToDate(date: Date, minutes: Int): Date? {
        val calendar = Calendar.getInstance()
        calendar.time = date
        calendar.add(Calendar.MINUTE, minutes)
        return calendar.time
    }


    fun getTimeDifferenceInHour(currentDate: Date, futureDate: Date): Int {

        val calendar = Calendar.getInstance()
        calendar.time = Date()

        val calendarFuture = Calendar.getInstance()
        calendarFuture.time = futureDate

        val difference: Long = calendarFuture.time.time - calendar.time.time
//        var days = (difference / (1000 * 60 * 60 * 24)).toInt()
//        var hours = ((difference - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60)).toInt()
//        var min = (difference - 1000 * 60 * 60 * 24 * days - 1000 * 60 * 60 * hours).toInt() / (1000 * 60)
        val diffDays: Long = difference / (24 * 60 * 60 * 1000)
        val diffSeconds: Long = difference / 1000 % 60
        val diffMinutes: Long = difference / (60 * 1000) % 60
        var diffHours: Long = difference / (60 * 60 * 1000) % 24
        printLog("Hour", "hours up :: $diffHours")
        printLog("Hour", "futureDate/ride date :: ${futureDate} currentDate ${currentDate}")
        printLog("Hour", "hours down :: $diffHours")
        return diffHours.toInt()

    }

    fun getTimeDifferenceInMinutes(currentDate: Date, futureDate: Date): Int {
        val difference: Long = futureDate.time - currentDate.time
//        var days = (difference / (1000 * 60 * 60 * 24)).toInt()
//        var hours = ((difference - 1000 * 60 * 60 * 24 * days) / (1000 * 60 * 60)).toInt()
//        var min = (difference - 1000 * 60 * 60 * 24 * days - 1000 * 60 * 60 * hours).toInt() / (1000 * 60)


        val diffDays: Long = difference / (24 * 60 * 60 * 1000)
        val diffSeconds: Long = difference / 1000 % 60
        var diffMinutes: Long = difference / (60 * 1000) % 60
        var diffHours: Long = difference / (60 * 60 * 1000) % 24
        printLog("Minutes", "Minutes up :: $diffMinutes")
//        diffMinutes = if (diffMinutes < 0) -diffMinutes else diffMinutes
        printLog("Minutes", "Minutes down :: $diffMinutes")
        printLog("Minutes", "futureDate/ride date :: ${futureDate} currentDate ${currentDate}")
        return diffMinutes.toInt()
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
