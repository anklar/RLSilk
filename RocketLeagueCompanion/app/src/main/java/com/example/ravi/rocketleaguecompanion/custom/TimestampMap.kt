package com.example.ravi.rocketleaguecompanion.custom

import com.example.ravi.rlcomp.custom.Timestamp
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class TimestampMap : TreeMap<Int, Timestamp>, Serializable {
    constructor(item: Timestamp) : super() {
        val dateString = SimpleDateFormat("yyyyMMdd").format(Date(item.time)).toInt()
        this[dateString] = item
    }

    constructor() : super()


    /**
     * adds a Timestamp to the map
     */
    fun put(item: Timestamp) {
        if(this.isEmpty())
            this[0] = item
        else
            this[item.day] = item
    }


    /**
     * gets the newest timestamp before the given day
     */
    fun getLastStampBefore(item: Timestamp): Timestamp {
        return this.lowerEntry(item.day)?.value ?: Timestamp(
                1526915296000, arrayOf(Ranking(0, 0, 0),
                Ranking(0, 0, 0), Ranking(0, 0, 0),
                Ranking(0, 0, 0)), 0, 0, 0, 0, 0, 0
        )
    }

}