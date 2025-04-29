package com.mediassist.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.mediassist.doctor.model.TimeSlot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import com.google.firebase.Timestamp

class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()

    /** Récupère les créneaux du médecin à partir de sa sous-collection */
    fun getDoctorSlotsFlow(doctorId: String): Flow<List<TimeSlot>> = callbackFlow {
        val sub = db
            .collection("doctors")
            .document(doctorId)
            .collection("slots")
            .addSnapshotListener { snap, _ ->
                val list = snap
                    ?.documents
                    ?.mapNotNull { it.toObject(TimeSlot::class.java) }
                    ?: emptyList()
                trySend(list).isSuccess
            }
        awaitClose { sub.remove() }
    }

    /** Récupère tous les créneaux non réservés via collectionGroup */
    fun getAvailableSlotsFlow(): Flow<List<TimeSlot>> = callbackFlow {
        val sub = db
            .collectionGroup("slots")
            .whereEqualTo("isBooked", false)
            .addSnapshotListener { snap, _ ->
                val list = snap
                    ?.documents
                    ?.mapNotNull { it.toObject(TimeSlot::class.java) }
                    ?: emptyList()
                trySend(list).isSuccess
            }
        awaitClose { sub.remove() }
    }

    /** Ajoute un créneau dans la sous-collection du médecin */
    suspend fun addSlot(
        doctorId: String,
        dateTime: Timestamp,
        isBooked: Boolean = false,
        patientId: String? = null
    ) {
        val slotsCol = db
            .collection("doctors")
            .document(doctorId)
            .collection("slots")
        val docRef = slotsCol.document()
        val data = mapOf(
            "id"        to docRef.id,
            "dateTime"  to dateTime,
            "isBooked"  to isBooked,
            "patientId" to patientId
        )
        docRef.set(data).await()
    }

    /** Réserve un créneau et marque isBooked=true dans la sous-collection */
    suspend fun bookSlot(
        doctorId: String,
        slotId: String,
        patientId: String
    ) {
        db
            .collection("doctors")
            .document(doctorId)
            .collection("slots")
            .document(slotId)
            .update(
                mapOf(
                    "isBooked"  to true,
                    "patientId" to patientId
                )
            ).await()
    }
}