package com.mediassist.ui.screens

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.mediassist.navigation.Routes

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DoctorStatusScreen(navController: NavController) {
    var selectedItem by remember { mutableStateOf(3) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFFE3F2FD), Color(0xFFB3E5FC))
                )
            )
    ) {
        // Unique TopAppBar
        TopAppBar(
            title = { Text("Statut Médecin") },
            actions = {
                IconButton(onClick = { /* TODO: search */ }) {
                    Icon(Icons.Filled.Search, contentDescription = "Recherche")
                }
                IconButton(onClick = {
                    navController.navigate(Routes.DOCTOR_PROFILE_VIEW)
                }) {
                    Icon(Icons.Filled.AccountCircle, contentDescription = "Voir profil")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
            modifier = Modifier
                .fillMaxWidth()
                .statusBarsPadding()
        )

        // Cercle + quadrant
        Box(
            modifier = Modifier
                .size(260.dp)
                .align(Alignment.Center)
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val r = size.minDimension / 2f
                drawCircle(color = Color(0xFFD0E8FF), radius = r)
                drawLine(color = Color.White, start = Offset(0f, center.y), end = Offset(size.width, center.y), strokeWidth = 4f)
                drawLine(color = Color.White, start = Offset(center.x, 0f), end = Offset(center.x, size.height), strokeWidth = 4f)
            }
            Column {
                Row {
                    QuadrantItem(Icons.Filled.MedicalServices, "Consultation") {}
                    QuadrantItem(Icons.Filled.Home, "Domicile") {}
                }
                Row {
                    QuadrantItem(Icons.Filled.AddAlert, "Urgence") {}
                    QuadrantItem(Icons.Filled.DoNotDisturbOn, "Ne pas déranger") {}
                }
            }
        }

        // Barre du bas
        BottomNavBar(
            selectedIndex = selectedItem,
            onItemSelected = { index ->
                selectedItem = index
                if (index == 3) {
                    navController.navigate(Routes.DOCTOR_APPTS)
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun QuadrantItem(icon: androidx.compose.ui.graphics.vector.ImageVector, label: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .size(130.dp)
            .clickable(onClick = onClick),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(icon, contentDescription = label, modifier = Modifier.size(36.dp))
        Spacer(Modifier.height(4.dp))
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun BottomNavBar(selectedIndex: Int, onItemSelected: (Int) -> Unit, modifier: Modifier = Modifier) {
    Row(
        modifier = modifier
            .background(Color(0xFFD0E8FF))
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        val items = listOf(
            Icons.Filled.Person to "Patients",
            Icons.Filled.SmartToy to "IA",
            Icons.Filled.Notifications to "Notifications",
            Icons.Filled.Event to "Rendez-vous"
        )
        items.forEachIndexed { index, (icon, label) ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .clickable { onItemSelected(index) }
                    .padding(horizontal = 8.dp)
            ) {
                Icon(icon, contentDescription = label, tint = if (selectedIndex == index) MaterialTheme.colorScheme.primary else Color.Gray)
                Text(label, style = MaterialTheme.typography.labelSmall, color = if (selectedIndex == index) MaterialTheme.colorScheme.primary else Color.Gray)
            }
        }
    }
}
