package com.example.ravi.rlcomp.custom

import com.example.ravi.rocketleaguecompanion.fragments.PlayerFragment


class TimestampList : ArrayList<Timestamp>{
    constructor(): super()
    constructor(item:Timestamp):super(){
        this.add(item)
    }

    /**
     * gets the newest timestamp before the given day
     */
    fun getLastStampBefore(day:Float):Timestamp?{
        if (PlayerFragment.getDayfromLong(this.last().time)< day) //check if day is beyond this list
            return null
        else{
            //find a stamp before the day with binary search
            var max = this.size
            var min = 0
            var index = max/2
            while(index != max){        //still searching
                val time = PlayerFragment.getDayfromLong(this[index].time)
                when {
                    time == day - 1 -> index = max          //matching stamp found
                    time <= day - 1 -> {                    //stamp that was to small found
                        max = index
                        index = min + (max - min) / 2
                    }
                    PlayerFragment.getDayfromLong(this[index + 1].time) == day -> index = max       //matching stamp, but not from the previous day
                    else -> {                                                                       //there is a stamp that is more fitting
                        min = index + 1
                        index = min + (max - min) / 2
                    }
                }
            }
            while(PlayerFragment.getDayfromLong(this[index+1].time) != day)         //skip until stamp on the last day before DAY is found
                index++
            return this[index]
        }
    }

    fun getGoals():Int{
        if(size==1)return this[0].goals
        return this[this.size-1].goals- this[0].goals
    }
    fun getShots():Int{
        if(size==1)return this[0].shots
        return this[this.size-1].shots- this[0].shots
    }
    fun getAssists():Int{
        if(size==1)return this[0].assists
        return this[this.size-1].assists- this[0].assists
    }
    fun getSaves():Int{
        if(size==1)return this[0].saves
        return this[this.size-1].saves-this[0].saves
    }
}