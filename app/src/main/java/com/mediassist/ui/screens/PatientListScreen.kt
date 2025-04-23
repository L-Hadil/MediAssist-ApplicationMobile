package com.mediassist.ui.screens



import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mediassist.navigation.Routes
import com.mediassist.viewmodel.PatientViewModel

@Composable
fun PatientListScreen(navController: NavController, viewModel: PatientViewModel) {
    val patients by viewModel.patients.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {
                navController.navigate(Routes.ADD_PATIENT)
            }) {
                Text("+")
            }
        }
    ) { padding ->
        Column(modifier = Modifier.padding(padding).padding(16.dp)) {
            Text("Liste des Patients", style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.height(16.dp))

            patients.forEach { patient ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp)
                        .clickable { /* à compléter */ }
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("${patient.prenom} ${patient.nom}")
                        Text("Date de naissance : ${patient.dateNaissance}")
                        Text("Antécédents : ${patient.antecedents}")
                    }
                }
            }
        }
    }
}
