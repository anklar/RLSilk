package com.example.ravi.rocketleaguecompanion.custom

import android.annotation.SuppressLint
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class Timestamp(var time: Long, val rankingList: Array<Ranking>,
                val shots: Int, val goals: Int, val saves: Int, val assists: Int, val wins: Int, val mvps: Int) : Serializable {
    val day: Int
        @SuppressLint("SimpleDateFormat")
        //Warning suggests to use local time instead, this could cause problem when a users locale time changes to yesterday when travelling
        get () = SimpleDateFormat("yyyyMMdd").format(Date(this.time)).toInt()
    val date: String
        @SuppressLint("SimpleDateFormat")
        //Warning suggests to use local time instead, this could cause problem when a users locale time changes to yesterday when travelling
        get () = SimpleDateFormat("dd.MM.yy").format(Date(this.time))

    /**
     * returns an array with mmr change in each queue
     */
    fun getRankingDifferential(old: Timestamp): Array<Int> {
        return arrayOf(
                this.rankingList[0].mmr - old.rankingList[0].mmr,
                this.rankingList[1].mmr - old.rankingList[1].mmr,
                this.rankingList[2].mmr - old.rankingList[2].mmr,
                this.rankingList[3].mmr - old.rankingList[3].mmr
        )
    }

    /**
     * returns the shot percentage since old Timestamp
     */
    fun getShotPerc(old: Timestamp): Float {
        return when ((shots.toFloat() - old.shots.toFloat())) {
            0f -> -1f
            else -> (goals.toFloat() - old.goals.toFloat()) / (shots.toFloat() - old.shots.toFloat()) * 100f
        }
    }

    /**
     * returns total shot percentage
     */
    fun getTotalShotPercentage(): Float {
        return when (shots) {
            0 -> 100f
            else -> goals.toFloat() / shots.toFloat() * 100f
        }
    }
}