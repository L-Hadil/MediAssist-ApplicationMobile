package com.mediassist.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mediassist.navigation.Routes
import com.mediassist.viewmodel.PatientViewModel
import com.mediassist.model.Patient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PatientListScreen(
    navController: NavController,
    viewModel: PatientViewModel
) {
    val patients by viewModel.patients.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Liste des Patients") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Routes.ADD_PATIENT)
            }) {
                Text("+")
            }
        }
    ) { padding ->
        LazyColumn(
            contentPadding = padding,
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(patients) { patient ->
                PatientListItem(
                    patient = patient,
                    onClick = {
                        navController.navigate("${Routes.PATIENT_DETAIL}/${patient.id}")
                    }
                )
            }
        }
    }
}

@Composable
fun PatientListItem(
    patient: Patient,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${patient.prenom} ${patient.nom}",
                style = MaterialTheme.typography.titleMedium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Date de naissance : ${patient.dateNaissance}",
                style = MaterialTheme.typography.bodyMedium
            )
            Text(
                text = "Antécédents : ${patient.antecedents}",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
