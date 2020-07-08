package com.practice.android_custom_datepicker

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.os.Build
import android.util.Log
import android.widget.DatePicker
import java.util.*

class DatePickerDialogWithMaxMinRange(context: Context?, callBack: OnDateSetListener?, minCalendar: Calendar, maxCalendar: Calendar, currentCalendar: Calendar) : DatePickerDialog(context!!, callBack, currentCalendar[Calendar.YEAR], currentCalendar[Calendar.MONTH], currentCalendar[Calendar.DAY_OF_MONTH]) {
    private var maxYear = 0
    private var maxMonth = 0
    private var maxDay = 0
    private var minYear = 0
    private var minMonth = 0
    private var minDay = 0
    private val minDateCalendar: Calendar? = minCalendar.clone() as Calendar
    private val maxDateCalendar: Calendar? = maxCalendar.clone() as Calendar
    private val calendar: Calendar? = currentCalendar.clone() as Calendar
    private var fired = false

    init {

        minDateCalendar?.let {
            minDay = it[Calendar.DAY_OF_MONTH]
            minMonth = it[Calendar.MONTH]
            minYear = it[Calendar.YEAR]
        }

        maxDateCalendar?.let {
            maxDay = it[Calendar.DAY_OF_MONTH]
            maxMonth = it[Calendar.MONTH]
            maxYear = it[Calendar.YEAR]
        }

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                fired = false
                this.datePicker.apply {
                    maxDate = maxDateCalendar?.time!!.time
                    minDate = minDateCalendar?.time!!.time
                }
            } else {
                fired = true
            }
        } catch (e: Throwable) {
            /*Have suppressed the exception
             * Time not between max and min date range
             * The exception comes when the selected date is few milliseconds more or less than min/max date in devices of android 3.0 and above
             */
            Log.d("maxDateCalendar", "" + maxDateCalendar?.time!!.time)
            Log.d("minDateCalendar", "" + minDateCalendar?.time!!.time)
            Log.d("calendar", "" + calendar?.time!!.time)
            e.printStackTrace()
        }
    }

    @SuppressLint("LongLogTag")
    override fun onDateChanged(view: DatePicker, year: Int, monthOfYear: Int, dayOfMonth: Int) {
        super.onDateChanged(view, year, monthOfYear, dayOfMonth)
        if (fired) {
            calendar?.let {
                it[Calendar.YEAR] = year
                it[Calendar.MONTH] = monthOfYear
                it[Calendar.DAY_OF_MONTH] =   dayOfMonth
            }

            if (calendar?.after(maxDateCalendar)!!) {
                fired = true
                if (maxMonth > 11) {
                    maxMonth = 11
                }
                view.updateDate(maxYear, maxMonth, maxDay)
                Log.d("In onDateChanged() method", "fired==>$fired")
            } else if (calendar.before(minDateCalendar)) {
                fired = true
                if (minMonth > 11) {
                    minMonth = 11
                }
                view.updateDate(minYear, minMonth, minDay)
                Log.d("In onDateChanged() method", "fired==>$fired")
            } else {
                fired = true
                view.updateDate(year, monthOfYear, dayOfMonth)
                Log.d("In onDateChanged() method", "fired==>$fired")
            }
        } else {
            Log.d("In onDateChanged() method", "fired==>$fired")
        }
    }
}