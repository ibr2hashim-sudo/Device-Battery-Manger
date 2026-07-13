package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AppDao {
    @Query("SELECT * FROM devices ORDER BY name ASC")
    fun getAllDevices(): Flow<List<Device>>

    @Query("SELECT * FROM devices WHERE id = :id LIMIT 1")
    fun getDeviceById(id: Int): Flow<Device?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDevice(device: Device): Long

    @androidx.room.Update
    suspend fun updateDevice(device: Device)

    @androidx.room.Delete
    suspend fun deleteDevice(device: Device)

    @Query("SELECT * FROM battery_change_logs WHERE deviceId = :deviceId ORDER BY changeDate DESC")
    fun getLogsForDevice(deviceId: Int): Flow<List<BatteryChangeLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLog(log: BatteryChangeLog)
    
    @Query("UPDATE devices SET lastBatteryChangeDate = :date WHERE id = :deviceId")
    suspend fun updateLastBatteryChangeDate(deviceId: Int, date: Long)
}
