package com.mediassist.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mediassist.ui.screens.AgendaScreen
import com.mediassist.ui.screens.PatientListScreen
import com.mediassist.ui.screens.AddPatientScreen
import com.mediassist.ui.screens.PatientDetailScreen
import com.mediassist.viewmodel.PatientViewModel

object Routes {
    const val AGENDA           = "agenda"
    const val PATIENT_LIST     = "patient_list"
    const val ADD_PATIENT      = "add_patient"
    const val PATIENT_DETAIL   = "patient_detail"
}

@Composable
fun AppNavGraph(
    navController: NavHostController,
    patientViewModel: PatientViewModel
) {
    NavHost(
        navController = navController,
        startDestination = Routes.PATIENT_LIST
    ) {
        // Agenda
        composable(Routes.AGENDA) {
            AgendaScreen()
        }

        // Liste des patients
        composable(Routes.PATIENT_LIST) {
            PatientListScreen(navController, patientViewModel)
        }

        // Ajouter un patient
        composable(Routes.ADD_PATIENT) {
            AddPatientScreen(navController, patientViewModel)
        }

        // Détail d’un patient, avec passage d’ID
        composable(
            route = "${Routes.PATIENT_DETAIL}/{patientId}",
            arguments = listOf(
                navArgument("patientId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val patientId = backStackEntry.arguments!!.getLong("patientId")
            val patient = patientViewModel.patients.value
                .first { it.id == patientId }
            PatientDetailScreen(patient)
        }
    }
}
