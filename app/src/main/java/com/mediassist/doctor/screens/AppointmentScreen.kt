// File: app/src/main/java/com/mediassist/doctor/screens/AppointmentScreen.kt
package com.mediassist.doctor.screens

import android.app.DatePickerDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddAlert
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.mediassist.doctor.model.Appointment
import com.mediassist.doctor.model.AppointmentType
import com.mediassist.doctor.model.AppointmentStatus
import com.mediassist.navigation.Routes
import com.mediassist.ui.theme.MediAssistTheme
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjusters

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppointmentScreen(
    navController: NavController,
    appointments: List<Appointment>,
    onAppointmentClick: (Appointment) -> Unit
) {
    val context = LocalContext.current
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }

    // Ouvre le DatePicker
    if (showDatePicker) {
        DatePickerDialog(
            context,
            { _, year, month, day ->
                selectedDate = LocalDate.of(year, month + 1, day)
                showDatePicker = false
            },
            selectedDate.year,
            selectedDate.monthValue - 1,
            selectedDate.dayOfMonth
        ).show()
    }

    val types = listOf(
        AppointmentType.CONSULTATION,
        AppointmentType.URGENCE,
        AppointmentType.VISITE_DOMICILE
    )
    var selectedTypeIndex by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Rendez-vous") },
                actions = {
                    IconButton(onClick = { showDatePicker = true }) {
                        Icon(Icons.Filled.CalendarToday, contentDescription = "Choisir une date")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                // Navigue vers l'écran d'ajout de créneau
                navController.navigate(Routes.CREATE_SLOT)
            }) {
                Icon(Icons.Filled.Add, contentDescription = "Ajouter un créneau")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFFF5F5F5))
                .padding(innerPadding)
        ) {
            // Onglets type de RDV
            TabRow(
                selectedTabIndex = selectedTypeIndex,
                containerColor = MaterialTheme.colorScheme.primaryContainer
            ) {
                types.forEachIndexed { index, type ->
                    Tab(
                        selected = index == selectedTypeIndex,
                        onClick = { selectedTypeIndex = index },
                        text = { Text(type.name.replace('_', ' ')) }
                    )
                }
            }

            // Date sélectionnée
            Text(
                text = selectedDate.format(DateTimeFormatter.ISO_DATE),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center
            )

            // Semaine autour de selectedDate
            val monday = selectedDate.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY))
            val week = remember(selectedDate) { (0..6).map { monday.plusDays(it.toLong()) } }
            val dateFormatter = DateTimeFormatter.ofPattern("EEE d")

            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                itemsIndexed(week) { _, date ->
                    val hasRdv = appointments.any { appt ->
                        appt.type == types[selectedTypeIndex] && appt.dateTime.toLocalDate() == date
                    }
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 8.dp)
                            .clickable { selectedDate = date }
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = dateFormatter.format(date),
                            color = if (date == selectedDate) MaterialTheme.colorScheme.primary else Color.Unspecified
                        )
                        Spacer(Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .background(
                                    color = if (hasRdv) MaterialTheme.colorScheme.primary else Color.LightGray,
                                    shape = MaterialTheme.shapes.small
                                )
                        )
                    }
                }
            }

            // Liste des RDV filtrés
            val filtered = appointments.filter { appt ->
                appt.type == types[selectedTypeIndex] && appt.dateTime.toLocalDate() == selectedDate
            }
            if (filtered.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = "Aucun rendez-vous ce jour-là",
                        style = MaterialTheme.typography.bodyLarge
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    itemsIndexed(filtered) { _, appt ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { onAppointmentClick(appt) },
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = when (appt.type) {
                                        AppointmentType.CONSULTATION -> Icons.Default.Person
                                        AppointmentType.URGENCE -> Icons.Default.AddAlert
                                        AppointmentType.VISITE_DOMICILE -> Icons.Default.Home
                                    },
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = appt.dateTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                                        style = MaterialTheme.typography.titleMedium
                                    )
                                    Text(
                                        text = appt.type.name.replace('_', ' '),
                                        style = MaterialTheme.typography.bodyMedium
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAppointmentScreen() {
    val navController = rememberNavController()
    // Sample data
    val sample = listOf(
        Appointment("1","pat","doc", AppointmentType.CONSULTATION, LocalDateTime.now().withHour(10).withMinute(0), AppointmentStatus.CONFIRME),
        Appointment("2","pat","doc", AppointmentType.VISITE_DOMICILE, LocalDateTime.now().plusDays(1).withHour(14).withMinute(30), AppointmentStatus.CONFIRME),
    )
    MediAssistTheme {
        AppointmentScreen(
            navController = navController,
            appointments = sample,
            onAppointmentClick = {}
        )
    }
}
