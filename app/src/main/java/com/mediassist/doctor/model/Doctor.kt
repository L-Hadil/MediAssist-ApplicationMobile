// File: Doctor.kt
package com.mediassist.doctor.model

data class Doctor(
    val id: String = "",                // Firestore document ID
    val nom: String = "",               // Nom complet du médecin
    val specialite: String = "",        // Ex: "Généraliste", "ORL"
    val adresse: String = "",           // Adresse du cabinet
    val disponibilites: Map<String, List<String>> = emptyMap() // ex: {"lundi": ["09:00", "10:00"]}
)
