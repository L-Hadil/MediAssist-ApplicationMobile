// File: app/src/main/java/com/mediassist/doctor/screens/CreateSlotScreen.kt
package com.mediassist.doctor.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mediassist.repository.FirestoreRepository
import com.mediassist.ui.theme.MediAssistTheme
import com.mediassist.viewmodel.DoctorSlotsViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateSlotScreen(
    viewModel: DoctorSlotsViewModel,  // *injection* depuis NavGraph
    navController: NavController
) {
    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var pickedDate by remember { mutableStateOf(LocalDate.now()) }
    var pickedTime by remember { mutableStateOf(LocalTime.now()) }

    val context = LocalContext.current

    Scaffold(
        topBar = { TopAppBar(title = { Text("Ajouter un créneau") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = { showDatePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Choisir la date : $pickedDate")
            }
            Button(
                onClick = { showTimePicker = true },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(text = "Choisir l'heure : $pickedTime")
            }
            Button(
                onClick = {
                    viewModel.addSlot(LocalDateTime.of(pickedDate, pickedTime))
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = !showDatePicker && !showTimePicker
            ) {
                Text("Enregistrer le créneau")
            }
        }

        if (showDatePicker) {
            DatePickerDialog(
                context,
                { _, y, m, d ->
                    pickedDate = LocalDate.of(y, m + 1, d)
                    showDatePicker = false
                },
                pickedDate.year,
                pickedDate.monthValue - 1,
                pickedDate.dayOfMonth
            ).show()
        }
        if (showTimePicker) {
            TimePickerDialog(
                context,
                { _, h, min ->
                    pickedTime = LocalTime.of(h, min)
                    showTimePicker = false
                },
                pickedTime.hour,
                pickedTime.minute,
                true
            ).show()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCreateSlotScreen() {
    val navController = rememberNavController()
    val fakeVm = DoctorSlotsViewModel(
        repo = FirestoreRepository(),
        doctorId = "docPreview"
    )
    MediAssistTheme {
        CreateSlotScreen(viewModel = fakeVm, navController = navController)
    }
}
