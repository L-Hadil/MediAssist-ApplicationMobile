package com.mediassist.doctor.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.mediassist.doctor.model.Doctor

object DoctorRepository {
    private val db = FirebaseFirestore.getInstance()
    private val doctorsRef = db.collection("doctors")

    /**
     * Récupère les médecins par spécialité depuis Firestore.
     */
    fun getDoctorsBySpeciality(
        specialite: String,
        onSuccess: (List<Doctor>) -> Unit,
        onFailure: (Exception) -> Unit
    ) {
        doctorsRef
            .whereEqualTo("specialite", specialite)
            .get()
            .addOnSuccessListener { snapshot ->
                val list = snapshot.documents.mapNotNull {
                    it.toObject(Doctor::class.java)
                }
                onSuccess(list)
            }
            .addOnFailureListener { e -> onFailure(e) }
    }
}