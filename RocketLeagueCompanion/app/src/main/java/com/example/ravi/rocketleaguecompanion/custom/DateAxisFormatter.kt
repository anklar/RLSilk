package com.example.ravi.rocketleaguecompanion.custom

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.text.SimpleDateFormat
import java.util.*


class DateAxisFormatter : IAxisValueFormatter {

    /**
     * converts a millis Timestamp into a formated String
     */
    override fun getFormattedValue(value: Float, axis: AxisBase): String {
        val time = value.toLong()
        return SimpleDateFormat("dd.MM.yy").format(Date(time))
    }
}