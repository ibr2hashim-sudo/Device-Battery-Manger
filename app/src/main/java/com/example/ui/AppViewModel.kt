package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.AppDatabase
import com.example.data.AppRepository
import com.example.data.BatteryChangeLog
import com.example.data.Device
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class AppViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: AppRepository

    init {
        val appDao = AppDatabase.getDatabase(application).appDao()
        repository = AppRepository(appDao)
    }

    val searchQuery = MutableStateFlow("")
    val filterDepartment = MutableStateFlow("")

    val allDevices: StateFlow<List<Device>> = repository.allDevices
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val filteredDevices: StateFlow<List<Device>> = combine(
        repository.allDevices,
        searchQuery,
        filterDepartment
    ) { devices, query, department ->
        devices.filter {
            (query.isEmpty() || it.name.contains(query, ignoreCase = true) || it.serialNumber.contains(query, ignoreCase = true)) &&
            (department.isEmpty() || it.department == department)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // For Device Detail Screen
    private val _selectedDeviceId = MutableStateFlow<Int?>(null)
    
    val selectedDevice: StateFlow<Device?> = combine(
        repository.allDevices,
        _selectedDeviceId
    ) { devices, id ->
        devices.find { it.id == id }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _deviceLogs = MutableStateFlow<List<BatteryChangeLog>>(emptyList())
    val deviceLogs: StateFlow<List<BatteryChangeLog>> = _deviceLogs.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    fun selectDevice(id: Int) {
        _selectedDeviceId.value = id
        viewModelScope.launch {
            repository.getLogsForDevice(id).collect {
                _deviceLogs.value = it
            }
        }
    }

    fun addDevice(department: String, assetId: String, name: String, manufacturer: String, model: String, serialNumber: String) {
        viewModelScope.launch {
            val device = Device(
                department = department,
                assetId = assetId,
                name = name,
                manufacturer = manufacturer,
                model = model,
                serialNumber = serialNumber,
                lastBatteryChangeDate = System.currentTimeMillis()
            )
            repository.insertDevice(device)
        }
    }

    fun updateDevice(device: Device, department: String, assetId: String, name: String, manufacturer: String, model: String, serialNumber: String) {
        viewModelScope.launch {
            val updatedDevice = device.copy(
                department = department,
                assetId = assetId,
                name = name,
                manufacturer = manufacturer,
                model = model,
                serialNumber = serialNumber
            )
            repository.updateDevice(updatedDevice)
        }
    }

    fun deleteDevice(device: Device) {
        viewModelScope.launch {
            repository.deleteDevice(device)
        }
    }

    fun addBatteryLog(notes: String) {
        val deviceId = _selectedDeviceId.value ?: return
        viewModelScope.launch {
            repository.addBatteryChangeLog(deviceId, System.currentTimeMillis(), notes)
        }
    }

    // Seed data if empty
    fun seedDataIfNeeded() {
        viewModelScope.launch {
            repository.allDevices.collect { devices ->
                if (devices.isEmpty()) {
                    addDevice("IT", "ASSET-001", "Laptop Pro", "Dell", "XPS 15", "SN-92837492")
                    addDevice("HR", "ASSET-002", "Office Phone", "Samsung", "Galaxy S22", "SN-10293847")
                    addDevice("Engineering", "ASSET-003", "Test Tablet", "Apple", "iPad Pro", "SN-56473829")
                }
            }
        }
    }
}
