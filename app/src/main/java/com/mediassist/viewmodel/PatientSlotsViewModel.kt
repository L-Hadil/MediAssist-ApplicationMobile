package com.mediassist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.mediassist.repository.FirestoreRepository
import com.mediassist.model.TimeSlot
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PatientSlotsViewModel(
    private val repo: FirestoreRepository = FirestoreRepository()
) : ViewModel() {
    /** Flux des créneaux disponibles */
    val slots: StateFlow<List<TimeSlot>> =
        repo.getAvailableSlotsFlow()
            .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

    /** Réserve un créneau */
    fun bookSlot(slotId: String) {
        val patientId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            repo.bookSlot(slotId, patientId)
        }
    }

    companion object {
        fun provideFactory(): ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    if (modelClass.isAssignableFrom(PatientSlotsViewModel::class.java)) {
                        @Suppress("UNCHECKED_CAST")
                        return PatientSlotsViewModel(repo = FirestoreRepository()) as T
                    }
                    throw IllegalArgumentException("Unknown ViewModel class")
                }
            }
    }
}
