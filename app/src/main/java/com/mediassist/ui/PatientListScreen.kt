package com.mediassist.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mediassist.model.Patient
import com.mediassist.viewmodel.PatientViewModel
import kotlinx.coroutines.flow.StateFlow
import androidx.compose.runtime.collectAsState

@Composable
fun PatientListScreen(viewModel: PatientViewModel) {
    // 1. On collecte la StateFlow
    val patients by viewModel.patients.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Liste des patients",
            fontSize = 24.sp,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        LazyColumn {
            // 2. On boucle sur la vraie liste
            items(patients) { patient: Patient ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "${patient.prenom} ${patient.nom}")
                        Text(text = "Date de naissance : ${patient.dateNaissance}")
                        Text(text = "Antécédents : ${patient.antecedents}")
                    }
                }
            }
        }
    }
}
