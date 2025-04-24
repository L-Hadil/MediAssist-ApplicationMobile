package com.mediassist.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mediassist.model.Patient
import com.mediassist.repository.PatientRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PatientViewModel : ViewModel() {
    private val repository = PatientRepository()

    private val _patients = MutableStateFlow<List<Patient>>(emptyList())
    val patients: StateFlow<List<Patient>> = _patients

    init {
        loadPatients()
    }

    fun loadPatients() {
        viewModelScope.launch {
            _patients.value = repository.getAllPatients()
        }
    }

    /** Ajoute ce bloc : */
    fun addPatient(patient: Patient) {
        // Ici on génère un ID automatique
        val newId = (_patients.value.maxOfOrNull { it.id } ?: 0L) + 1L
        _patients.value = _patients.value + patient.copy(id = newId)
    }
}
