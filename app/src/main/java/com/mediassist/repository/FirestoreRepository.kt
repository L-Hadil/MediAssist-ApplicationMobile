package com.mediassist.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.mediassist.model.TimeSlot
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import com.google.firebase.Timestamp

class FirestoreRepository {
    private val db = FirebaseFirestore.getInstance()

    fun getDoctorSlotsFlow(doctorId: String): Flow<List<TimeSlot>> = callbackFlow {
        val sub = db.collection("slots")
            .whereEqualTo("doctorId", doctorId)
            .addSnapshotListener { snap, _ ->
                val list = snap?.documents
                    ?.mapNotNull { it.toObject(TimeSlot::class.java) }
                    ?: emptyList()
                trySend(list).isSuccess
            }
        awaitClose { sub.remove() }
    }

    fun getAvailableSlotsFlow(): Flow<List<TimeSlot>> = callbackFlow {
        val sub = db.collection("slots")
            .whereEqualTo("isBooked", false)
            .addSnapshotListener { snap, _ ->
                val list = snap?.documents
                    ?.mapNotNull { it.toObject(TimeSlot::class.java) }
                    ?: emptyList()
                trySend(list).isSuccess
            }
        awaitClose { sub.remove() }
    }

    suspend fun addSlot(
        doctorId: String,
        dateTime: Timestamp,
        isBooked: Boolean = false,
        patientId: String? = null
    ) {
        val docRef = db.collection("slots").document()
        val data = mapOf(
            "id"        to docRef.id,
            "doctorId"  to doctorId,
            "dateTime"  to dateTime,
            "isBooked"  to isBooked,
            "patientId" to patientId
        )
        docRef.set(data).await()
    }


    suspend fun bookSlot(slotId: String, patientId: String) {
        db.collection("slots").document(slotId)
            .update(
                mapOf(
                    "isBooked"  to true,
                    "patientId" to patientId
                )
            ).await()
    }
}
