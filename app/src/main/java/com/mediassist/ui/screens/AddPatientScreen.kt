package com.mediassist.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mediassist.model.Patient
import com.mediassist.viewmodel.PatientViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddPatientScreen(
    navController: NavController,
    viewModel: PatientViewModel,
    existingPatient: Patient? = null // nullable pour édition future
) {
    // États pour chaque champ
    var nom by remember { mutableStateOf(existingPatient?.nom.orEmpty()) }
    var prenom by remember { mutableStateOf(existingPatient?.prenom.orEmpty()) }
    var dateNaissance by remember { mutableStateOf(existingPatient?.dateNaissance.orEmpty()) }
    var antecedents by remember { mutableStateOf(existingPatient?.antecedents.orEmpty()) }

    // Gestion date picker
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = existingPatient?.let {
            Instant.parse(it.dateNaissance + "T00:00:00Z").toEpochMilli()
        } ?: Instant.now().toEpochMilli()
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let { millis ->
                        val instant = Instant.ofEpochMilli(millis)
                        dateNaissance = instant.atZone(ZoneId.systemDefault())
                            .toLocalDate().format(DateTimeFormatter.ISO_DATE)
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

    Scaffold(
        topBar = {
            TopAppBar(title = {
                Text(if (existingPatient == null) "Ajouter un patient" else "Modifier le patient")
            })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            OutlinedTextField(
                value = nom,
                onValueChange = { nom = it },
                label = { Text("Nom") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = prenom,
                onValueChange = { prenom = it },
                label = { Text("Prénom") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = dateNaissance,
                onValueChange = { /* non modifiable directement */ },
                label = { Text("Date de naissance") },
                readOnly = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { showDatePicker = true }
            )
            OutlinedTextField(
                value = antecedents,
                onValueChange = { antecedents = it },
                label = { Text("Antécédents médicaux") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                singleLine = false,
                maxLines = 5
            )

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (nom.isNotBlank() && prenom.isNotBlank() && dateNaissance.isNotBlank()) {
                        val patient = Patient(
                            nom = nom,
                            prenom = prenom,
                            dateNaissance = dateNaissance,
                            antecedents = antecedents
                        )
                        viewModel.addPatient(patient)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.align(Alignment.End)
            ) {
                Text("Enregistrer")
            }
        }
    }
}
