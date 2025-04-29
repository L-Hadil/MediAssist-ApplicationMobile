// File: MainActivity.kt
package com.mediassist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseAuth
import com.mediassist.navigation.AppNavGraph
import com.mediassist.ui.theme.MediAssistTheme
import com.mediassist.viewmodel.DoctorSlotsViewModel
import com.mediassist.viewmodel.PatientSlotsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MediAssistTheme {
                val navController = rememberNavController()

                // UID du user (médecin ou patient)
                val uid = FirebaseAuth.getInstance().currentUser?.uid ?: ""

                // VM Médecin
                val doctorSlotsViewModel: DoctorSlotsViewModel = viewModel(
                    factory = DoctorSlotsViewModel.provideFactory(uid)
                )

                // VM Patient
                val patientSlotsViewModel: PatientSlotsViewModel = viewModel(
                    factory = PatientSlotsViewModel.provideFactory()
                )

                AppNavGraph(
                    navController = navController,
                    doctorSlotsViewModel = doctorSlotsViewModel,
                    patientSlotsViewModel = patientSlotsViewModel,
                    appointments = emptyList(),
                    onAppointmentClick = {}
                )
            }
        }
    }
}
