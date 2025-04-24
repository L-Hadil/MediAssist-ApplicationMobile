package com.mediassist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

// Modèle d'un rendez-vous
data class Appointment(
    val id: Long,
    val patientName: String,
    val time: String,
    val reason: String,
    val date: LocalDate
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgendaScreen() {
    // État pour le dialogue de sélection de date
    var showDatePicker by remember { mutableStateOf(false) }
    val today = LocalDate.now()
    var selectedDate by remember { mutableStateOf(today) }

    // État du DatePicker
    val datePickerState = rememberDatePickerState(initialSelectedDateMillis =
    Instant.now().toEpochMilli()
    )

    // Dialogue DatePicker
    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val instant = Instant.ofEpochMilli(millis)
                        selectedDate = instant.atZone(ZoneId.systemDefault()).toLocalDate()
                    }
                    showDatePicker = false
                }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(state = datePickerState)
        }
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        Text(
            text = "Agenda - ${selectedDate.format(DateTimeFormatter.ofPattern("dd MMMM yyyy"))}",
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedButton(onClick = { showDatePicker = true }) {
            Text("Sélectionner une date")
        }
        Spacer(modifier = Modifier.height(16.dp))

        // Liste des rendez-vous mockés
        val appointments = remember { getMockAppointments() }
            .filter { it.date == selectedDate }

        if (appointments.isEmpty()) {
            Text(
                text = "Aucun rendez-vous pour cette date.",
                fontSize = 16.sp,
                modifier = Modifier.padding(top = 24.dp)
            )
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(appointments) { appointment ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = appointment.time, fontSize = 18.sp)
                            Text(text = appointment.patientName, fontSize = 20.sp)
                            Text(text = appointment.reason, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}


fun getMockAppointments(): List<Appointment> = listOf(
    Appointment(1, "Dupont Alice", "09:30", "Consultation générale", LocalDate.now()),
    Appointment(2, "Martin Jean", "14:00", "Suivi de traitement", LocalDate.now()),
    Appointment(3, "Durand Claire", "11:00", "Contrôle annuel", LocalDate.now().plusDays(1))
)
