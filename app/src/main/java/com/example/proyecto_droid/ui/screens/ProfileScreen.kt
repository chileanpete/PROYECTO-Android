package com.example.proyecto_droid.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.proyecto_droid.R
import com.example.proyecto_droid.ui.theme.GreenPrimary
import com.example.proyecto_droid.ui.theme.LightGrayText

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = androidx.compose.ui.graphics.Color(0xFFF8F8F8))
    ) {
        // Encabezado con imagen y nombre
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
                .background(color = GreenPrimary),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Surface(
                    shape = CircleShape,
                    shadowElevation = 8.dp,
                    modifier = Modifier
                        .size(90.dp)
                        .offset(y = 24.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.ic_profile),
                        contentDescription = "Foto de perfil",
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Spacer(modifier = Modifier.height(36.dp))
                Text(
                    text = "Miranda West",
                    style = MaterialTheme.typography.titleLarge.copy(color = androidx.compose.ui.graphics.Color.White, fontSize = 22.sp)
                )
                Text(
                    text = "Work hard in silence. Let your success be the noise.",
                    style = MaterialTheme.typography.bodyMedium.copy(color = LightGrayText, fontSize = 14.sp),
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(32.dp))
        // Opciones de perfil
        ProfileOptionCard(icon = R.drawable.ic_events, text = "My Address")
        ProfileOptionCard(icon = R.drawable.ic_launcher_foreground, text = "Account")
        ProfileOptionCard(icon = R.drawable.ic_launcher_foreground, text = "Notifications")
        ProfileOptionCard(icon = R.drawable.ic_launcher_foreground, text = "Devices")
        ProfileOptionCard(icon = R.drawable.ic_launcher_foreground, text = "Passwords")
        ProfileOptionCard(icon = R.drawable.ic_launcher_foreground, text = "Language")
    }
}

@Composable
fun ProfileOptionCard(icon: Int, text: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 6.dp)
            .clickable { /* Acción al pulsar */ },
        shape = RoundedCornerShape(14.dp),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(18.dp)
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = text,
                tint = GreenPrimary,
                modifier = Modifier.size(26.dp)
            )
            Spacer(modifier = Modifier.width(18.dp))
            Text(text, style = MaterialTheme.typography.bodyLarge.copy(color = androidx.compose.ui.graphics.Color.Black))
        }
    }
} 