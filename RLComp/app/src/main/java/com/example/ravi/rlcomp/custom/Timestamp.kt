package com.example.ravi.rlcomp.custom

class Timestamp(val time:Long, val matchesPlayed:Int, val rankingList:Array<Ranking>,
                val shots:Int, val goals:Int, val saves:Int, val assists:Int, val wins:Int, val mvps:Int){
    fun getShotPerc(old:Timestamp):Float{
        return return (goals.toFloat()-old.goals.toFloat())     /    (shots.toFloat()-old.shots.toFloat())
    }
    fun getTotalShotPercentage():Float{
        return goals.toFloat()/shots.toFloat()
    }
}