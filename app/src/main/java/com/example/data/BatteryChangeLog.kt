package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "battery_change_logs")
data class BatteryChangeLog(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val deviceId: Int,
    val changeDate: Long,
    val notes: String = ""
)
