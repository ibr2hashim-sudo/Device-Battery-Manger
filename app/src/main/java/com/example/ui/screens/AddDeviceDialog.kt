package com.example.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.data.Device

@Composable
fun DeviceFormDialog(
    initialDevice: Device? = null,
    onDismiss: () -> Unit,
    onConfirm: (department: String, assetId: String, name: String, manufacturer: String, model: String, serialNumber: String) -> Unit
) {
    var department by remember { mutableStateOf(initialDevice?.department ?: "") }
    var assetId by remember { mutableStateOf(initialDevice?.assetId ?: "") }
    var name by remember { mutableStateOf(initialDevice?.name ?: "") }
    var manufacturer by remember { mutableStateOf(initialDevice?.manufacturer ?: "") }
    var model by remember { mutableStateOf(initialDevice?.model ?: "") }
    var serialNumber by remember { mutableStateOf(initialDevice?.serialNumber ?: "") }

    val isEditing = initialDevice != null

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (isEditing) "تعديل بيانات الجهاز" else "إضافة جهاز جديد") },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
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
            }
        },
        confirmButton = {
            Button(onClick = { onConfirm(department, assetId, name, manufacturer, model, serialNumber) }) {
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
