package com.example.ravi.rlcomp.custom

import com.example.ravi.rlcomp.PlayerActivity

class TimestampList : ArrayList<Timestamp>{
    constructor(): super()
    constructor(item:Timestamp):super(){
        this.add(item)
    }

    /**
     * gets the newest timestamp before the given day
     */
    fun getLastStampBefore(day:Float):Timestamp?{
        if (PlayerActivity.Companion.getDayfromLong(this.last().time)< day) //check if day is beyond this list
            return null
        else{
            //find a stamp before the day with binary search
            var max = this.size
            var min = 0
            var index = max/2
            while(index != max){        //still searching
                if()//TODO implement binary search
            }
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