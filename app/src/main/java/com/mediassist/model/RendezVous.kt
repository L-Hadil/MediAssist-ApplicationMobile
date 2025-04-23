package com.mediassist.model

data class RendezVous(
    val id: Long = 0,
    val date: String,
    val heure: String,
    val motif: String,
    val patientId: Long
)