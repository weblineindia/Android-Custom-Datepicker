package com.practice.android_custom_datepicker

import android.app.Activity
import android.app.DatePickerDialog
import android.app.DatePickerDialog.OnDateSetListener
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AlertDialog
import com.practice.android_custom_datepicker.Util.Util
import com.practice.android_custom_datepicker.Util.Util.getDate
import kotlinx.android.synthetic.main.custom_datepickerdemo.*
import java.util.*

class CustomDatepickerdemo : Activity() {
    private var context: Context? = null
    private var calendar: Calendar? = null
    private var maxDate: String? = null
    private var minDate: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.custom_datepickerdemo)
        context = this@CustomDatepickerdemo
        calendar = Calendar.getInstance()

        maxDate = Util.getSharedPrefValue(this, Util.MAX_DATE_VALUE)
        minDate = Util.getSharedPrefValue(this, Util.MIN_DATE_VALUE)

        if (maxDate != null && !maxDate.equals("", ignoreCase = true)) {
            tvSelectMaxDatePicker!!.text = maxDate
        }
        if (minDate != null && !minDate.equals("", ignoreCase = true)) {
            tvSelectMinDatePicker!!.text = minDate
        }

        btnSelectDate!!.setOnClickListener(onClick)
        tvSelectMaxDatePicker!!.setOnClickListener(onClick)
        tvSelectMinDatePicker!!.setOnClickListener(onClick)
    }

    private var onClick = View.OnClickListener { v ->

        when(v.id){
            R.id.btnSelectDate -> {
                openDatePickerWithMaxAndMindate(tvSelectMinDatePicker!!.text.toString(), tvSelectMaxDatePicker!!.text.toString())
            }
            R.id.tvSelectMaxDatePicker -> {
                showMaxDatePickerDialog()
            }
            R.id.tvSelectMinDatePicker -> {
                showMinDatePickerDialog()
            }
        }
    }

    private var datePickerListener = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        calendar?.let {
            it[Calendar.YEAR] = year
            it[Calendar.MONTH] = monthOfYear
            it[Calendar.DAY_OF_MONTH] = dayOfMonth
        }

        btnSelectDate!!.text = getDate(calendar!!.time)
    }

    /**
     * Show date picker dialog for selecting minimum Date
     */
    private fun showMinDatePickerDialog() {
        if (tvSelectMinDatePicker!!.text.toString().equals(getString(R.string.lbl_select_date), ignoreCase = true)) {
            calendar = Calendar.getInstance()
        } else {
            val selectedDate = tvSelectMinDatePicker!!.text.toString()
            calendar?.let {
                it[Calendar.DAY_OF_MONTH] = selectedDate.split("-").toTypedArray()[0].toInt()
                it[Calendar.MONTH] = selectedDate.split("-").toTypedArray()[1].toInt() - 1
                it[Calendar.YEAR] = selectedDate.split("-").toTypedArray()[2].toInt()
            }
        }
        DatePickerDialog(context!!, minDatePickerListener, calendar!![Calendar.YEAR], calendar!![Calendar.MONTH], calendar!![Calendar.DAY_OF_MONTH]).show()
    }

    /**
     * This is the DatePicker listener for Date of Birth, invoked when user select the date.
     */
    var minDatePickerListener = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        try {
            val selectedDate: Date
            var maxDate: Date? = null

            calendar?.let {
                it[Calendar.YEAR] = year
                it[Calendar.MONTH] = monthOfYear
                it[Calendar.DAY_OF_MONTH] = dayOfMonth
            }

            val date = calendar!!.timeInMillis
            selectedDate = Date(date)
            if (tvSelectMaxDatePicker!!.text != null && tvSelectMaxDatePicker!!.text.isNotEmpty() && !tvSelectMaxDatePicker!!.text.toString().equals(getString(R.string.lbl_select_date), ignoreCase = true)) {
                maxDate = getDate(tvSelectMaxDatePicker!!.text.toString())
            }
            if (maxDate != null && maxDate.before(selectedDate)) {
                // Reset the todate if it is smaller than selected date
                tvSelectMinDatePicker!!.text = getDate(calendar!!.time)
                tvSelectMaxDatePicker!!.text = getString(R.string.lbl_select_date)
            } else {
                tvSelectMinDatePicker!!.text = getDate(calendar!!.time)
                Util.putSharedPrefValue(this, Util.MIN_DATE_VALUE, getDate(calendar!!.time))
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * Show date picker dialog for selecting to Date
     */
    private fun showMaxDatePickerDialog() {
        if (tvSelectMaxDatePicker!!.text.toString().equals(getString(R.string.lbl_select_date), ignoreCase = true)) {
            calendar = Calendar.getInstance()
        } else {
            val selectedDate = tvSelectMaxDatePicker!!.text.toString()
            calendar?.let {
                it[Calendar.DAY_OF_MONTH] = selectedDate.split("-").toTypedArray()[0].toInt()
                it[Calendar.MONTH] = selectedDate.split("-").toTypedArray()[1].toInt() - 1
                it[Calendar.YEAR] = selectedDate.split("-").toTypedArray()[2].toInt()
            }

        }
        DatePickerDialog(context!!, maxDatePickerListener, calendar!![Calendar.YEAR], calendar!![Calendar.MONTH], calendar!![Calendar.DAY_OF_MONTH]).show()
    }

    /**
     * This is the DatePicker listener for Date of Birth, invoked when user select the date.
     */
    private var maxDatePickerListener = OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
        try {
            val selectedDate: Date
            var minDate: Date? = null

            calendar?.let {
                it[Calendar.YEAR] = year
                it[Calendar.MONTH] = monthOfYear
                it[Calendar.DAY_OF_MONTH] = dayOfMonth
            }

            val date = calendar!!.timeInMillis
            selectedDate = Date(date)
            if (tvSelectMinDatePicker!!.text != null && tvSelectMinDatePicker!!.text.length > 0 && !tvSelectMinDatePicker!!.text.toString().equals(getString(R.string.lbl_select_date), ignoreCase = true)) {
                minDate = getDate(tvSelectMinDatePicker!!.text as String)
            }

            // TODO need to check if to date and from date must be same or not
            if (minDate != null && selectedDate.before(minDate)) {
                val alert = AlertDialog.Builder(context!!).apply {
                    setTitle(getString(R.string.app_name))
                    setMessage(getString(R.string.tv_invalidDateMessage))
                    setPositiveButton("Ok") { dialog, which -> showMaxDatePickerDialog() }
                }.show()

            } else {
                tvSelectMaxDatePicker!!.text = getDate(calendar!!.time)
                Util.putSharedPrefValue(this, Util.MAX_DATE_VALUE, getDate(calendar!!.time))
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    /**
     * Method to show the datepicker dialog with predefined max and min range
     * @param minDate The minimum date for the datepicker
     * @param maxDate The maximum date for the datepicker
     */
    private fun openDatePickerWithMaxAndMindate(minDate: String, maxDate: String) {
        try {
            val minYear: Int
            val minMonth: Int
            val minDay: Int
            val maxYear: Int
            val maxMonth: Int
            val maxDay: Int
            var emptyInvalidDate = true
            emptyInvalidDate = if (tvSelectMaxDatePicker!!.text.toString() != null && !tvSelectMaxDatePicker!!.text.toString().equals("", ignoreCase = true) ||
                    tvSelectMinDatePicker!!.text.toString() != null && !tvSelectMinDatePicker!!.text.toString().equals("", ignoreCase = true)) {
                false
            } else {
                true
            }
            emptyInvalidDate = if (tvSelectMaxDatePicker!!.text.toString().equals(getString(R.string.lbl_select_date), ignoreCase = true) ||
                    tvSelectMinDatePicker!!.text.toString().equals(getString(R.string.lbl_select_date), ignoreCase = true)) {
                true
            } else {
                false
            }
            if (emptyInvalidDate) {
                val alert = AlertDialog.Builder(context!!).apply {
                    setTitle(getString(R.string.app_name))
                    setMessage(getString(R.string.tv_invalidDateMessage))
                    setPositiveButton("Ok") { dialog, which -> }
                }.show()

            } else {
                minDay = minDate.split("-").toTypedArray()[0].toInt()
                minMonth = minDate.split("-").toTypedArray()[1].toInt() - 1
                minYear = minDate.split("-").toTypedArray()[2].toInt()
                maxDay = maxDate.split("-").toTypedArray()[0].toInt()
                maxMonth = maxDate.split("-").toTypedArray()[1].toInt() - 1
                maxYear = maxDate.split("-").toTypedArray()[2].toInt()

                val maxDateCalendar = Calendar.getInstance().also {
                    it[Calendar.YEAR] = maxYear
                    it[Calendar.MONTH] = maxMonth
                    it[Calendar.DAY_OF_MONTH] = maxDay
                }

                val minDateCalendar = Calendar.getInstance().also {
                    it[Calendar.YEAR] = minYear
                    it[Calendar.MONTH] = minMonth
                    it[Calendar.DAY_OF_MONTH] = minDay
                }

                if (btnSelectDate!!.text.toString().equals(getString(R.string.lbl_select_date), ignoreCase = true)) {
                    calendar = Calendar.getInstance()
                } else {
                    val selectedDate = btnSelectDate!!.text.toString()
                    calendar?.let {
                        it[Calendar.DAY_OF_MONTH] = selectedDate.split("-").toTypedArray()[0].toInt()
                        it[Calendar.MONTH] = selectedDate.split("-").toTypedArray()[1].toInt() - 1
                        it[Calendar.YEAR] = selectedDate.split("-").toTypedArray()[2].toInt()
                    }

                }
                if (minDateCalendar.after(calendar)) {
                    DatePickerDialogWithMaxMinRange(context, datePickerListener, minDateCalendar, maxDateCalendar, minDateCalendar).show()
                } else if (maxDateCalendar.before(calendar)) {
                    DatePickerDialogWithMaxMinRange(context, datePickerListener, minDateCalendar, maxDateCalendar, maxDateCalendar).show()
                } else {
                    DatePickerDialogWithMaxMinRange(context, datePickerListener, minDateCalendar, maxDateCalendar, calendar!!).show()
                }
            }
        } catch (e: Throwable) {
            // Have suppressed the exception
            e.printStackTrace()
        }
    }
}