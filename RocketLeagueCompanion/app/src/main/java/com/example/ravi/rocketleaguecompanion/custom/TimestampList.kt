package com.example.ravi.rlcomp.custom

import java.text.SimpleDateFormat
import java.util.*

class TimestampList : TreeMap<Int, Timestamp> {
    constructor(item: Timestamp) : super() {
        val dateString = SimpleDateFormat("yyyyMMdd").format(Date(item.time)).toInt()
        this[dateString] = item
    }
    constructor() : super()



    fun put(item: Timestamp) {
        this[item.day] = item
    }


    /**
     * gets the newest timestamp before the given day
     */
    fun getLastStampBefore(item: Timestamp): Timestamp {
       return this.lowerEntry(item.day)?.value ?:Timestamp(
               1526915296000, 0, arrayOf(Ranking(0, 0, 0),
                Ranking(0, 0, 0), Ranking(0, 0, 0),
                Ranking(0, 0, 0)), 0, 0, 0, 0, 0, 0
        )
    }

}