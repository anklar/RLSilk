package com.example.ravi.rlcomp.custom

import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

class Timestamp(val time:Long, val matchesPlayed:Int, val rankingList:Array<Ranking>,
                val shots:Int, val goals:Int, val saves:Int, val assists:Int, val wins:Int, val mvps:Int):Serializable{
    val day:Int
        get () = SimpleDateFormat("yyyyMMdd").format(Date(this.time)).toInt()
    fun getShotPerc(old:Timestamp):Float{
        return (goals.toFloat()-old.goals.toFloat())     /    (shots.toFloat()-old.shots.toFloat())*100f
    }
    fun getTotalShotPercentage():Float{
        return goals.toFloat()/shots.toFloat()*100f
    }
}