package com.mediassist.doctor.screens

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts.GetContent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringArrayResource
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.mediassist.R
import com.mediassist.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorProfileFormScreen(navController: NavController) {
    val context = LocalContext.current
    val auth    = FirebaseAuth.getInstance()
    val db      = FirebaseFirestore.getInstance()
    val storage = Firebase.storage.reference

    var firstName by remember { mutableStateOf("") }
    var lastName  by remember { mutableStateOf("") }
    var email     by remember { mutableStateOf("") }
    var phone     by remember { mutableStateOf("") }
    var password  by remember { mutableStateOf("") }
    var specialty by remember { mutableStateOf("") }
    var address   by remember { mutableStateOf("") }
    var photoUri  by remember { mutableStateOf<Uri?>(null) }

    // image picker
    val launcher = rememberLauncherForActivityResult(GetContent()) { uri ->
        photoUri = uri
    }

    val specialties = stringArrayResource(R.array.doctor_specialties).toList()
    var expanded by remember { mutableStateOf(false) }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Inscription Médecin") })
    }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Photo
            Box(
                modifier = Modifier
                    .size(100.dp)
                    .align(Alignment.CenterHorizontally)
                    .clip(CircleShape)
                    .clickable { launcher.launch("image/*") },
                contentAlignment = Alignment.Center
            ) {
                if (photoUri != null) {
                    AsyncImage(
                        model = photoUri,
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Text("Ajouter\nphoto", textAlign = TextAlign.Center)
                }
            }

            // Prénom
            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                label = { Text("Prénom") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Nom
            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                label = { Text("Nom") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Email
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Téléphone
            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Téléphone") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Mot de passe
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Mot de passe") },
                visualTransformation = PasswordVisualTransformation(),
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Dropdown spécialité
            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = specialty,
                    onValueChange = { /* no-op */ },
                    label = { Text("Spécialité") },
                    readOnly = true,
                    trailingIcon = {
                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .menuAnchor()
                )
                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    specialties.forEach { spec ->
                        DropdownMenuItem(
                            text = { Text(spec) },
                            onClick = {
                                specialty = spec
                                expanded = false
                            }
                        )
                    }
                }
            }

            // Adresse cabinet
            OutlinedTextField(
                value = address,
                onValueChange = { address = it },
                label = { Text("Adresse cabinet") },
                singleLine = true,
                modifier = Modifier.fillMaxWidth()
            )

            // Bouton Enregistrer
            Button(
                onClick = {
                    if (listOf(firstName, lastName, email, password, specialty, address).any { it.isBlank() }) {
                        Toast.makeText(context, "Remplis tous les champs", Toast.LENGTH_SHORT).show()
                        return@Button
                    }
                    auth.createUserWithEmailAndPassword(email, password)
                        .addOnSuccessListener { res ->
                            val uid = res.user!!.uid
                            val data = hashMapOf(
                                "id" to uid,
                                "firstName" to firstName,
                                "lastName" to lastName,
                                "email" to email,
                                "phone" to phone,
                                "specialty" to specialty,
                                "address" to address,
                                "photoUrl" to ""
                            )
                            fun saveProfile() {
                                db.collection("doctors").document(uid).set(data)
                            }
                            if (photoUri != null) {
                                storage.child("doctors/$uid/profile.jpg")
                                    .putFile(photoUri!!)
                                    .continueWithTask { storage.child("doctors/$uid/profile.jpg").downloadUrl }
                                    .addOnSuccessListener { url ->
                                        data["photoUrl"] = url.toString()
                                        saveProfile()
                                    }
                            } else saveProfile()

                            Toast.makeText(context, "Profil enregistré", Toast.LENGTH_SHORT).show()
                            navController.navigate(Routes.DOCTOR_HOME) {
                                popUpTo(Routes.DOCTOR_FORM) { inclusive = true }
                            }
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(context, "Erreur : ${e.message}", Toast.LENGTH_LONG).show()
                        }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Enregistrer")
            }
        }
    }
}
