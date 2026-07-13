package com.example.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import coil.compose.AsyncImage
import com.example.data.Device
import java.io.File

@Composable
fun DeviceFormDialog(
    initialDevice: Device? = null,
    onDismiss: () -> Unit,
    onConfirm: (department: String, assetId: String, name: String, manufacturer: String, model: String, serialNumber: String, batteryVolt: String, batteryAmpere: String, deviceImageUri: String?, batteryImageUri: String?) -> Unit
) {
    var department by remember { mutableStateOf(initialDevice?.department ?: "") }
    var assetId by remember { mutableStateOf(initialDevice?.assetId ?: "") }
    var name by remember { mutableStateOf(initialDevice?.name ?: "") }
    var manufacturer by remember { mutableStateOf(initialDevice?.manufacturer ?: "") }
    var model by remember { mutableStateOf(initialDevice?.model ?: "") }
    var serialNumber by remember { mutableStateOf(initialDevice?.serialNumber ?: "") }
    var batteryVolt by remember { mutableStateOf(initialDevice?.batteryVolt ?: "") }
    var batteryAmpere by remember { mutableStateOf(initialDevice?.batteryAmpere ?: "") }
    var deviceImageUri by remember { mutableStateOf(initialDevice?.deviceImageUri) }
    var batteryImageUri by remember { mutableStateOf(initialDevice?.batteryImageUri) }

    val isEditing = initialDevice != null
    val context = LocalContext.current

    // Launchers for Device Image
    var tempDeviceUri by remember { mutableStateOf<Uri?>(null) }
    val deviceCameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && tempDeviceUri != null) {
            deviceImageUri = tempDeviceUri.toString()
        }
    }
    val deviceGalleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            deviceImageUri = uri.toString()
        }
    }

    // Launchers for Battery Image
    var tempBatteryUri by remember { mutableStateOf<Uri?>(null) }
    val batteryCameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success && tempBatteryUri != null) {
            batteryImageUri = tempBatteryUri.toString()
        }
    }
    val batteryGalleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            batteryImageUri = uri.toString()
        }
    }

    fun createImageUri(): Uri {
        val file = File(context.cacheDir, "images").apply { mkdirs() }
        val tempFile = File.createTempFile("IMG_", ".jpg", file)
        return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", tempFile)
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEditing) "تعديل بيانات الجهاز" else "إضافة جهاز جديد") },
        text = {
            Column(modifier = Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
                OutlinedTextField(value = department, onValueChange = { department = it }, label = { Text("القسم") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = assetId, onValueChange = { assetId = it }, label = { Text("ID") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("اسم الجهاز") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = manufacturer, onValueChange = { manufacturer = it }, label = { Text("الشركة المصنعة") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = model, onValueChange = { model = it }, label = { Text("الموديل") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = serialNumber, onValueChange = { serialNumber = it }, label = { Text("الرقم التسلسلي") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = batteryVolt, onValueChange = { batteryVolt = it }, label = { Text("فولت البطارية (مثال: 12V)") }, modifier = Modifier.fillMaxWidth())
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(value = batteryAmpere, onValueChange = { batteryAmpere = it }, label = { Text("أمبير البطارية (مثال: 2000mAh)") }, modifier = Modifier.fillMaxWidth())
                
                Spacer(modifier = Modifier.height(16.dp))
                Text("صورة الجهاز", style = MaterialTheme.typography.labelMedium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { 
                        tempDeviceUri = createImageUri()
                        deviceCameraLauncher.launch(tempDeviceUri!!)
                    }) {
                        Icon(Icons.Default.CameraAlt, contentDescription = "كاميرا")
                    }
                    IconButton(onClick = { deviceGalleryLauncher.launch("image/*") }) {
                        Icon(Icons.Default.Image, contentDescription = "معرض الصور")
                    }
                    if (deviceImageUri != null) {
                        AsyncImage(model = deviceImageUri, contentDescription = null, modifier = Modifier.size(48.dp).clip(MaterialTheme.shapes.small), contentScale = ContentScale.Crop)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
                Text("صورة البطارية", style = MaterialTheme.typography.labelMedium)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    IconButton(onClick = { 
                        tempBatteryUri = createImageUri()
                        batteryCameraLauncher.launch(tempBatteryUri!!)
                    }) {
                        Icon(Icons.Default.CameraAlt, contentDescription = "كاميرا")
                    }
                    IconButton(onClick = { batteryGalleryLauncher.launch("image/*") }) {
                        Icon(Icons.Default.Image, contentDescription = "معرض الصور")
                    }
                    if (batteryImageUri != null) {
                        AsyncImage(model = batteryImageUri, contentDescription = null, modifier = Modifier.size(48.dp).clip(MaterialTheme.shapes.small), contentScale = ContentScale.Crop)
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(department, assetId, name, manufacturer, model, serialNumber, batteryVolt, batteryAmpere, deviceImageUri, batteryImageUri) }) {
                Text("حفظ")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}
