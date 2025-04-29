package com.mediassist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.google.firebase.Timestamp
import com.mediassist.model.TimeSlot
import com.mediassist.repository.FirestoreRepository
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.Date

class DoctorSlotsViewModel(
    private val repo: FirestoreRepository = FirestoreRepository(),
    private val doctorId: String
) : ViewModel() {

    /** Flux des créneaux du médecin */
    val slots: StateFlow<List<TimeSlot>> = repo
        .getDoctorSlotsFlow(doctorId)
        .stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    /**
     * Ajoute un créneau :
     * - convertit LocalDateTime → Instant → java.util.Date → com.google.firebase.Timestamp
     * - appelle le repo
     */
    fun addSlot(dateTime: LocalDateTime) {
        val instant = dateTime.atZone(ZoneId.systemDefault()).toInstant()
        val ts = Timestamp(Date.from(instant))
        viewModelScope.launch {
            repo.addSlot(doctorId = doctorId, dateTime = ts)
        }
    }

    companion object {
        /** Factory pour injecter le doctorId dans le ViewModel */
        fun provideFactory(doctorId: String): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(DoctorSlotsViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return DoctorSlotsViewModel(
                            repo = FirestoreRepository(),
                            doctorId = doctorId
                        ) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
                }
            }
    }
}
