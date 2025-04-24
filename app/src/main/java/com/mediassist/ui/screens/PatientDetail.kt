package com.mediassist.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mediassist.model.Patient

@Composable
fun PatientDetailScreen(patient: Patient) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Infos", "Historique", "Ordonnances")

    Column(modifier = Modifier.fillMaxSize()) {
        // En-tête avec nom complet
        Text(
            text = "${patient.prenom} ${patient.nom}",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(16.dp)
        )

        // Onglets
        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index }
                ) {
                    Text(text = title, modifier = Modifier.padding(16.dp))
                }
            }
        }

        // Contenu selon l'onglet
        when (selectedTab) {
            0 -> InfoTab(patient)
            1 -> HistoryTab()
            2 -> PrescriptionsTab()
        }
    }
}

@Composable
private fun InfoTab(patient: Patient) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Antécédents médicaux:", style = MaterialTheme.typography.titleMedium)
        Text(text = patient.antecedents, modifier = Modifier.padding(top = 8.dp), fontSize = 16.sp)
        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Examens:", style = MaterialTheme.typography.titleMedium)
        Text(
            text = "• Examen sanguin\n• Radiographie thoracique",
            modifier = Modifier.padding(top = 8.dp),
            fontSize = 16.sp
        )
    }
}

@Composable
private fun HistoryTab() {
    val history = remember {
        listOf(
            "01/01/2025 - Consultation générale",
            "15/02/2025 - Suivi de traitement",
            "10/03/2025 - Contrôle annuel"
        )
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(history) { entry ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = entry,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
private fun PrescriptionsTab() {
    val prescriptions = remember {
        listOf(
            "Ordonnance A - 05/01/2025",
            "Ordonnance B - 20/02/2025"
        )
    }
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(prescriptions) { pres ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = pres,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp
                )
            }
        }
    }
}
