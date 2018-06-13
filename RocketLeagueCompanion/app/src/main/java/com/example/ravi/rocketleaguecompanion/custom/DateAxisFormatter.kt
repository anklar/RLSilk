package com.example.ravi.rocketleaguecompanion.custom

import android.annotation.SuppressLint
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.*


class DateAxisFormatter : IAxisValueFormatter {

    /**
     * converts a millis Timestamp into a formated String
     */
    @SuppressLint("SimpleDateFormat")
    //Warning suggests to use local time instead, this could cause problem when a users locale time changes to yesterday when travelling
    override fun getFormattedValue(value: Float, axis: AxisBase): String {
        val time = value.toLong()
        return SimpleDateFormat("dd.MM.yy").format(Date(time))
    }
}