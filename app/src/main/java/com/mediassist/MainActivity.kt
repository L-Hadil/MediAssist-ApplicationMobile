package com.mediassist

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.MaterialTheme
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.rememberNavController
import com.google.firebase.firestore.FirebaseFirestore
import com.mediassist.navigation.AppNavGraph
import com.mediassist.ui.theme.MediAssistTheme
import com.mediassist.viewmodel.DoctorSlotsViewModel
import com.mediassist.viewmodel.PatientSlotsViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
      //  FirebaseFirestore.getInstance().useEmulator("10.0.2.2", 8080)

        setContent {
            MediAssistTheme {
                val navController = rememberNavController()

                // 🚀 Ici tu passes un doctorId FAUX pour tester (par exemple "doctor123")
                val doctorId = "doctor123"

                // 🚀 Utiliser les factories pour injecter les paramètres
                val doctorSlotsViewModel: DoctorSlotsViewModel = viewModel(
                    factory = DoctorSlotsViewModel.provideFactory(doctorId)
                )

                val patientSlotsViewModel: PatientSlotsViewModel = viewModel(
                    factory = PatientSlotsViewModel.provideFactory()
                )


                AppNavGraph(
                    navController = navController,
                    doctorSlotsViewModel = doctorSlotsViewModel,
                    patientSlotsViewModel = patientSlotsViewModel,
                    appointments = listOf(
                        // ➡️ Mets ici tes données Appointment si tu veux
                    ),
                    onAppointmentClick = { /* TODO: clique sur un rendez-vous */ }
                )
            }
        }
    }
}
