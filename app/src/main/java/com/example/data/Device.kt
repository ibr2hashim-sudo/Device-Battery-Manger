package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "devices")
data class Device(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val department: String,
    val assetId: String, // User's ID
    val name: String,
    val manufacturer: String,
    val model: String,
    val serialNumber: String,
    val lastBatteryChangeDate: Long
)
