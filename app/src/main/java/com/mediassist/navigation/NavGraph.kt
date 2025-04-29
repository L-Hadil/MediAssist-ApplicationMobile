package com.mediassist.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import com.mediassist.doctor.screens.DoctorSlotsScreen     // ← add this
import com.mediassist.doctor.screens.AppointmentScreen
import com.mediassist.doctor.screens.CreateSlotScreen
import com.mediassist.patient.screens.SlotSelectionScreen // ← and this
import com.mediassist.patient.screens.RoleSelectionScreen
import com.mediassist.ui.screens.DoctorStatusScreen
import com.mediassist.viewmodel.DoctorSlotsViewModel
import com.mediassist.viewmodel.PatientSlotsViewModel
import com.mediassist.model.Appointment

object Routes {
    const val ROLE_SELECTION   = "role_selection"
    const val DOCTOR_HOME      = "doctor_home"
    const val DOCTOR_SLOTS     = "doctor_slots"
    const val DOCTOR_APPTS     = "doctor_appointments"
    const val PATIENT_HOME     = "patient_home"
    const val PATIENT_SLOTS    = "patient_slots"
    const val CREATE_SLOT      = "doctor-cretslot"
    const val PATIENT_APPTS    = "patient_appointments"
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    doctorSlotsViewModel: DoctorSlotsViewModel,
    patientSlotsViewModel: PatientSlotsViewModel,
    appointments: List<Appointment>,
    onAppointmentClick: (Appointment) -> Unit
) {
    NavHost(navController = navController, startDestination = Routes.ROLE_SELECTION) {
        composable(Routes.ROLE_SELECTION) {
            RoleSelectionScreen(navController = navController)
        }

        // Médecin flow
        composable(Routes.DOCTOR_HOME) {
            DoctorStatusScreen(navController = navController)
        }
        composable(Routes.DOCTOR_SLOTS) {
            DoctorSlotsScreen(
                viewModel = doctorSlotsViewModel,
                navController = navController
            )
        }
        composable(Routes.DOCTOR_APPTS) {
            AppointmentScreen(
                navController = navController,
                appointments = appointments,
                onAppointmentClick = onAppointmentClick
            )
        }
        composable(Routes.CREATE_SLOT) {
            CreateSlotScreen(navController = navController)
        }

        // Patient flow
        composable(Routes.PATIENT_HOME) {
            SlotSelectionScreen(
                viewModel = patientSlotsViewModel,
                navController = navController
            )
        }
        composable(Routes.PATIENT_SLOTS) {
            SlotSelectionScreen(
                viewModel = patientSlotsViewModel,
                navController = navController
            )
        }
        composable(Routes.PATIENT_APPTS) {
            AppointmentScreen(
                navController = navController,
                appointments = appointments,
                onAppointmentClick = onAppointmentClick
            )
        }
    }
}
