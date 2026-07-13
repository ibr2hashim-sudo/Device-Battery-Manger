package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.AppViewModel
import com.example.ui.screens.DeviceFormDialog
import com.example.ui.screens.DeviceDetailScreen
import com.example.ui.screens.MainScreen
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContent {
      MyApplicationTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            BatteryApp()
        }
      }
    }
  }
}

@Composable
fun BatteryApp() {
    val navController = rememberNavController()
    val viewModel: AppViewModel = viewModel()
    
    LaunchedEffect(Unit) {
        viewModel.seedDataIfNeeded()
    }

    var showAddDialog by remember { mutableStateOf(false) }

    if (showAddDialog) {
        DeviceFormDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { dept, id, name, manufacturer, model, serial ->
                viewModel.addDevice(dept, id, name, manufacturer, model, serial)
                showAddDialog = false
            }
        )
    }

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(
                viewModel = viewModel,
                onNavigateToDetail = { deviceId ->
                    viewModel.selectDevice(deviceId)
                    navController.navigate("detail")
                },
                onAddDevice = { showAddDialog = true }
            )
        }
        composable("detail") {
            DeviceDetailScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() }
            )
        }
    }
}
