package com.mediassist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mediassist.doctor.model.Appointment
import com.mediassist.doctor.screens.*
import com.mediassist.patient.screens.RoleSelectionScreen
import com.mediassist.patient.screens.SlotSelectionScreen
import com.mediassist.ui.screens.DoctorStatusScreen
import com.mediassist.viewmodel.DoctorSlotsViewModel
import com.mediassist.viewmodel.PatientSlotsViewModel

object Routes {
    const val ROLE_SELECTION      = "role_selection"
    const val DOCTOR_FORM         = "doctor_form"
    const val DOCTOR_PROFILE_VIEW = "doctor_profile_view"
    const val DOCTOR_HOME         = "doctor_home"
    const val DOCTOR_SLOTS        = "doctor_slots"
    const val DOCTOR_APPTS        = "doctor_appointments"
    const val CREATE_SLOT         = "doctor-cretslot"
    const val PATIENT_HOME        = "patient_home"
    const val PATIENT_SLOTS       = "patient_slots"
    const val PATIENT_APPTS       = "patient_appointments"
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
            RoleSelectionScreen(navController)
        }

        // FORM Médecin
        composable(Routes.DOCTOR_FORM) {
            DoctorProfileFormScreen(navController)
        }

        // AFFICHAGE PROFIL Médecin
        composable(Routes.DOCTOR_PROFILE_VIEW) {
            DoctorProfileViewScreen(navController)
        }

        // ACCUEIL Médecin
        composable(Routes.DOCTOR_HOME) {
            DoctorStatusScreen(navController)
        }

        // ↓ ICI, on passe bien **notre** doctorSlotsViewModel
        composable(Routes.DOCTOR_SLOTS) {
            DoctorSlotsScreen(
                viewModel     = doctorSlotsViewModel,
                navController = navController
            )
        }

        // ↓ Pareil pour la création de créneau
        composable(Routes.CREATE_SLOT) {
            CreateSlotScreen(
                viewModel     = doctorSlotsViewModel,
                navController = navController
            )
        }
        composable(Routes.DOCTOR_APPTS) {
            AppointmentScreen(
                navController     = navController,
                appointments      = appointments,
                onAppointmentClick = onAppointmentClick
            )
        }

        // Patient flow
        composable(Routes.PATIENT_HOME) {
            SlotSelectionScreen(patientSlotsViewModel, navController)
        }
        composable(Routes.PATIENT_SLOTS) {
            SlotSelectionScreen(patientSlotsViewModel, navController)
        }
        composable(Routes.PATIENT_APPTS) {
            AppointmentScreen(navController, appointments, onAppointmentClick)
        }
    }
}
