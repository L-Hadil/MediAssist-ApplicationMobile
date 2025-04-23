package com.mediassist.repository

import com.mediassist.model.Patient
import com.mediassist.network.RetrofitClient

class PatientRepository {
    suspend fun getAllPatients(): List<Patient> {
        // Remplacer plus tard par RetrofitClient.apiService.getPatients()
        return listOf(
            Patient(1, "Alice", "Dupont", "1990-01-01", "Diabète"),
            Patient(2, "Jean", "Martin", "1985-07-23", "Hypertension")
        )
    }
}
