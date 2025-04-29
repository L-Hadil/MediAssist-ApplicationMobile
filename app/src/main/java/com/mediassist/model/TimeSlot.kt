package com.mediassist.model

import com.google.firebase.Timestamp

/**
 * Représente un créneau horaire.
 * @param dateTime Timestamp Firebase (UTC)
 */
data class TimeSlot(
    val id: String = "",
    val doctorId: String = "",
    val dateTime: Timestamp? = null,
    val isBooked: Boolean = false,
    val patientId: String? = null
)
