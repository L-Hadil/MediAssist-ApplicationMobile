package com.mediassist.model
data class Patient(
    val id: String = "",               // Firestore document ID
    val nom: String = "",
    val prenom: String = "",
    val dateNaissance: String = "",    // ISO-8601 (ex. "1980-05-12")
    val antecedents: List<String> = emptyList(),
    val contact: ContactInfo = ContactInfo()
)