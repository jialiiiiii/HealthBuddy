package com.example.healthbuddy.post

import java.text.SimpleDateFormat

open class DtClass {

    val dateFormat = SimpleDateFormat("dd-mm-yyyy")

    fun millisecondsToDate(milliseconds:Long,dateFormat: SimpleDateFormat):String{
        return dateFormat.format(milliseconds)
    }

    fun dateToMilliseconds(date:String,dateFormat: SimpleDateFormat):Long{
        val mDate = dateFormat.parse(date)
        return mDate.time
    }
}