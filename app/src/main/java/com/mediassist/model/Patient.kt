package com.mediassist.model

data class Patient(
    val id: Long = 0,
    val nom: String,
    val prenom: String,
    val dateNaissance: String,
    val antecedents: String
)