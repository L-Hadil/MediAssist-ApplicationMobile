package com.mediassist.doctor.screens

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mediassist.model.TimeSlot
import com.google.firebase.Timestamp
import com.mediassist.viewmodel.DoctorSlotsViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorSlotsScreen(
    viewModel: DoctorSlotsViewModel,
    navController: NavController
) {
    val slots by viewModel.slots.collectAsState()
    val context = LocalContext.current

    var showDatePicker by remember { mutableStateOf(false) }
    var showTimePicker by remember { mutableStateOf(false) }
    var pickedDate by remember { mutableStateOf(LocalDate.now()) }
    var pickedTime by remember { mutableStateOf(LocalTime.now()) }

    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

    Scaffold(
        topBar = { TopAppBar(title = { Text("Mes créneaux") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDatePicker = true }) {
                Icon(Icons.Default.Add, contentDescription = "Ajouter créneau")
            }
        }
    ) { padding ->
        Box(Modifier.padding(padding)) {
            LazyColumn {
                items(slots) { slot ->
                    // Conversion Firestore Timestamp → LocalDateTime
                    val localDT = slot.dateTime
                        ?.toDate()
                        ?.toInstant()
                        ?.atZone(ZoneId.systemDefault())
                        ?.toLocalDateTime()

                    Card(
                        Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    ) {
                        Row(
                            Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = localDT?.format(formatter) ?: "—",
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Spacer(Modifier.weight(1f))
                            if (slot.isBooked) {
                                Text("Réservé", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }

            // DatePicker
            if (showDatePicker) {
                DatePickerDialog(
                    context,
                    { _, year, month, day ->
                        pickedDate = LocalDate.of(year, month + 1, day)
                        showDatePicker = false
                        showTimePicker = true
                    },
                    pickedDate.year,
                    pickedDate.monthValue - 1,
                    pickedDate.dayOfMonth
                ).show()
            }

            // TimePicker
            if (showTimePicker) {
                TimePickerDialog(
                    context,
                    { _, hour, minute ->
                        pickedTime = LocalTime.of(hour, minute)
                        showTimePicker = false
                        // Envoi au ViewModel
                        viewModel.addSlot(LocalDateTime.of(pickedDate, pickedTime))
                    },
                    pickedTime.hour,
                    pickedTime.minute,
                    true
                ).show()
            }
        }
    }
}
