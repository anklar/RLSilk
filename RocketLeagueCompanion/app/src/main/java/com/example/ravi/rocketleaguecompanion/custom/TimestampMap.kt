package com.example.ravi.rocketleaguecompanion.custom

import java.io.Serializable
import java.util.*

class TimestampMap : TreeMap<Int, Timestamp>, Serializable {
    constructor(item: Timestamp) : super() {
        this[0] = item
        item.time = item.time -86400000
    }

    constructor() : super()


    /**
     * adds a Timestamp to the map
     */
    fun put(item: Timestamp) {
        if (this.isEmpty())
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