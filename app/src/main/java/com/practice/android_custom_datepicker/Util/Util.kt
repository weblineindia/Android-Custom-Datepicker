package com.practice.android_custom_datepicker.Util

import android.content.Context
import android.content.SharedPreferences
import com.practice.android_custom_datepicker.R
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

object Util {
    private var dateFormat: SimpleDateFormat? = null
    private var spDatePickerPref: SharedPreferences? = null
    const val MAX_DATE_VALUE = "MAX_DATE_VALUE"
    const val MIN_DATE_VALUE = "MIN_DATE_VALUE"

    fun putSharedPrefValue(context: Context, key: String, value: String){
        if (spDatePickerPref == null) {
            spDatePickerPref = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        }
        val spDatePickerPrefEdit = spDatePickerPref?.edit()
        spDatePickerPrefEdit?.putString(key, value)
        spDatePickerPrefEdit?.apply()
    }

    fun getSharedPrefValue(context: Context, key: String): String? {
        if (spDatePickerPref == null) {
            spDatePickerPref = context.getSharedPreferences(context.getString(R.string.app_name), Context.MODE_PRIVATE)
        }
        return spDatePickerPref!!.getString(key, null)
    }

    fun getDate(date: Date?): String {
        dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        return dateFormat!!.format(date)
    }

    @JvmStatic
    fun getDate(date: String?): Date? {
        var newDate: Date? = null
        dateFormat = SimpleDateFormat("dd-MM-yyyy", Locale.ENGLISH)
        try {
            newDate = dateFormat!!.parse(date)
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return newDate
    }
}