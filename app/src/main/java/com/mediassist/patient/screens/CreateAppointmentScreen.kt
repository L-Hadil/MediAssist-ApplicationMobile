package com.mediassist.patient.screens

import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mediassist.doctor.model.Doctor
import com.mediassist.doctor.repository.DoctorRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateAppointmentScreen(
    navController: NavController,
    onDoctorSelected: (Doctor) -> Unit
) {
    // 1) Liste des spécialités
    val specialities = listOf("Généraliste", "ORL", "Pédiatre")
    var expanded by remember { mutableStateOf(false) }
    var selectedSpec by remember { mutableStateOf(specialities.first()) }

    // 2) Chargement des médecins
    var doctors by remember { mutableStateOf(listOf<Doctor>()) }
    LaunchedEffect(selectedSpec) {
        DoctorRepository.getDoctorsBySpeciality(
            selectedSpec,
            onSuccess = { list -> doctors = list },
            onFailure = { /* gérer erreur */ }
        )
    }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp)
    ) {
        // Dropdown spécialités
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = it }
        ) {
            TextField(
                value = selectedSpec,
                onValueChange = {},
                label = { Text("Spécialité") },
                readOnly = true,
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded) },
                modifier = Modifier.fillMaxWidth()
            )
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                specialities.forEach { spec ->
                    DropdownMenuItem(
                        text = { Text(spec) },
                        onClick = {
                            selectedSpec = spec
                            expanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Liste des médecins filtrés
        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(doctors) { doctor ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = doctor.nom, style = MaterialTheme.typography.titleMedium)
                        Text(text = doctor.specialite, style = MaterialTheme.typography.bodyMedium)
                        Text(text = doctor.adresse, style = MaterialTheme.typography.bodySmall)

                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { onDoctorSelected(doctor) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Prendre rendez-vous")
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CreateAppointmentScreenPreview() {
    CreateAppointmentScreen(
        navController = rememberNavController(),
        onDoctorSelected = {}
    )
}
