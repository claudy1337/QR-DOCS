package com.autonture.qrdocs.db.converters

import androidx.room.TypeConverter
import java.util.*



class DateTimeConverters {
    @TypeConverter
    fun toCalendar(l: Long): Calendar? {
        val c = Calendar.getInstance()
        c!!.timeInMillis = l
        return c
    }

    @TypeConverter
    fun fromCalendar(c: Calendar?): Long? {
        return c?.time?.time
    }
}