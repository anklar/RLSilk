package com.example.ravi.rlcomp.custom

import com.example.ravi.rocketleaguecompanion.fragments.PlayerFragment
import java.text.SimpleDateFormat
import java.util.*

//TODO REFRACTOR TO IMPLEENT SORTEDMAP
class TimestampList : HashMap<Int,Timestamp>{
    constructor(item:Timestamp):super(){
        val dateString = SimpleDateFormat("yyyyMMdd").format(Date(item.time)).toInt()
        this[dateString] = item
    }

    fun put(item:Timestamp){
        val dateString = SimpleDateFormat("yyyyMMdd").format(Date(item.time)).toInt()
        this[item.day] = item
    }


    /**
     * gets the newest timestamp before the given day
     */
    fun getLastStampBefore(item:Timestamp):Timestamp?{
        if(this.contains(item.day-1))           //in case a stamp exactly a day before item is in the list
            return this[item.day-1]
        val max = this.keys.max()
        if ( max == null || ( max >= item.day)) //check if day is beyond this list
            return null
        else{
            //the most recent stamp before "today" isn't yesterday
            for( i in item.day-2.. (this.keys?.min() ?:20180517))
                if(this.contains(i))
                    return this[i]
            return null
        }
    }

}