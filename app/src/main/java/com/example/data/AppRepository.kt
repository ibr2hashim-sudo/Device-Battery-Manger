package com.example.data

import kotlinx.coroutines.flow.Flow

class AppRepository(private val appDao: AppDao) {
    val allDevices: Flow<List<Device>> = appDao.getAllDevices()

    fun getDeviceById(id: Int): Flow<Device?> = appDao.getDeviceById(id)

    suspend fun insertDevice(device: Device): Long = appDao.insertDevice(device)

    suspend fun updateDevice(device: Device) = appDao.updateDevice(device)

    suspend fun deleteDevice(device: Device) = appDao.deleteDevice(device)

    fun getLogsForDevice(deviceId: Int): Flow<List<BatteryChangeLog>> = appDao.getLogsForDevice(deviceId)

    suspend fun addBatteryChangeLog(deviceId: Int, changeDate: Long, notes: String) {
        val log = BatteryChangeLog(deviceId = deviceId, changeDate = changeDate, notes = notes)
        appDao.insertLog(log)
        appDao.updateLastBatteryChangeDate(deviceId, changeDate)
    }
}
