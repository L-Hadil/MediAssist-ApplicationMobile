// File: Appointment.kt
package com.mediassist.model

import java.time.LocalDateTime

data class Appointment(
    val id: String = "",                   // Firestore document ID
    val patientId: String = "",            // Référence à Patient.id
    val doctorId: String = "",             // Référence à Doctor.id
    val type: AppointmentType = AppointmentType.CONSULTATION,
    val dateTime: LocalDateTime = LocalDateTime.now(),
    val statut: AppointmentStatus = AppointmentStatus.EN_ATTENTE,
    val adresse: String? = null,           // Pour la visite à domicile
    val commentaire: String? = null        // Infos complémentaires du patient
)
