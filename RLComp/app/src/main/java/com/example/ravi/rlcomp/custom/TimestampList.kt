package com.example.ravi.rlcomp.custom

class TimestampList : ArrayList<Timestamp>{
    constructor(): super()
    constructor(item:Timestamp):super(){
        this.add(item)
    }

    fun getByDay(day:Long):TimestampList{
        var result = TimestampList()
        for( stamp:Timestamp in this){
            if(stamp.time in day..day+86400000)
                result.add(stamp)
        }
        return result
    }
     fun getShotPercentage():Double{
         return getGoals().toDouble()/getShots()
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