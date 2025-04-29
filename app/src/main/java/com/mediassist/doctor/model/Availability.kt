package com.mediassist.doctor.model

data class Availability(
    val jour: String = "",    // ex. "Lundi" ou une date précise
    val debut: String = "",   // "HH:mm"
    val fin: String = ""      // "HH:mm"
)