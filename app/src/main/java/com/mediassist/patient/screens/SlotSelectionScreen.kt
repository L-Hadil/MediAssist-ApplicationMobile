package com.mediassist.patient.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController                              // <-- NavController, pas NavHostController
import com.mediassist.viewmodel.PatientSlotsViewModel
import com.mediassist.navigation.Routes                           // <-- pour la route
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlotSelectionScreen(
    viewModel: PatientSlotsViewModel,
    navController: NavController
) {
    // On lit le StateFlow des créneaux dispos
    val slots by viewModel.slots.collectAsState()
    val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")

    Scaffold(
        topBar = { TopAppBar(title = { Text("Créneaux disponibles") }) }
    ) { padding ->
        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            if (slots.isEmpty()) {
                item {
                    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Text("Aucun créneau disponible")
                    }
                }
            } else {
                items(slots) { slot ->
                    // Convertit le Timestamp Firebase en LocalDateTime
                    val localDT = slot.dateTime
                        ?.toDate()
                        ?.toInstant()
                        ?.atZone(ZoneId.systemDefault())
                        ?.toLocalDateTime()

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable {
                                // 1) Réserve le créneau
                                viewModel.bookSlot(slot)
                                // 2) Navigue vers la liste des RDV du patient
                                navController.navigate(Routes.PATIENT_APPTS)
                            },
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        )
                    ) {
                        Text(
                            text = localDT?.format(formatter) ?: "—",
                            Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}
