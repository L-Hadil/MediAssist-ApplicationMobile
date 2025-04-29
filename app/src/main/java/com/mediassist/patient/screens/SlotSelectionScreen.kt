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
import androidx.navigation.NavHostController
import com.mediassist.viewmodel.PatientSlotsViewModel
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SlotSelectionScreen(
    viewModel: PatientSlotsViewModel,
    navController: NavHostController
) {
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
                    val localDT = slot.dateTime
                        ?.toDate()
                        ?.toInstant()
                        ?.atZone(ZoneId.systemDefault())
                        ?.toLocalDateTime()

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { viewModel.bookSlot(slot.id) },
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
