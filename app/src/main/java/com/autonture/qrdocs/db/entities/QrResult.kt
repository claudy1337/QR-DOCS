package com.autonture.qrdocs.db.entities

import androidx.room.*
import com.autonture.qrdocs.db.converters.DateTimeConverters
import java.sql.Timestamp
import java.util.*

/**
 * Developed by Happy on 5/7/19
 */
@Entity
@TypeConverters(DateTimeConverters::class)
data class QrResult(

    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,

    @ColumnInfo(name = "result")
    val result: String?,

    @ColumnInfo(name = "result_type")
    val resultType: String ,

    @ColumnInfo(name = "favourite")
    val favourite: Boolean = false,

    @ColumnInfo(name = "time")
    val calendar: Calendar

)