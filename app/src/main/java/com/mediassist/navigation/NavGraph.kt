package com.mediassist.navigation


import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.mediassist.ui.PatientListScreen
import com.mediassist.ui.screens.AddPatientScreen
import com.mediassist.ui.screens.PatientListScreen
import com.mediassist.viewmodel.PatientViewModel

object Routes {
    const val PATIENT_LIST = "patient_list"
    const val ADD_PATIENT = "add_patient"
}

@Composable
fun AppNavGraph(navController: NavHostController, patientViewModel: PatientViewModel) {
    NavHost(navController = navController, startDestination = Routes.PATIENT_LIST) {
        composable(Routes.PATIENT_LIST) {
            PatientListScreen(navController, patientViewModel)
        }
        composable(Routes.ADD_PATIENT) {
            AddPatientScreen(navController, patientViewModel)
        }
    }
}
