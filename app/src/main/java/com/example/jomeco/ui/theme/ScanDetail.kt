package com.example.jomeco.ui.theme

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.jomeco.network.Product
import com.example.jomeco.network.RetrofitClientOFF
import com.example.jomeco.R



@Composable
fun ProductDetail(
    type: String,
    bin: String,
    isDangerous: Boolean,
    bitmap: Bitmap?,
    navController: NavController // Pass navController to enable navigation
) {
    var showBinDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Back Button (top-left)
        IconButton(
            onClick = { navController.popBackStack() },
            modifier = Modifier.align(Alignment.TopStart)
        ) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back"
            )
        }

        // Close Button (top-right)
        IconButton(
            onClick = { navController.navigate("home") }, // Or wherever you want to go
            modifier = Modifier.align(Alignment.TopEnd)
        ) {
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Close"
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 48.dp), // So content doesn't overlap with buttons
            verticalArrangement = Arrangement.spacedBy(20.dp, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image Card
            Card(
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
            ) {
                bitmap?.let {
                    Image(
                        bitmap = it.asImageBitmap(),
                        contentDescription = "Captured Image",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } ?: Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.LightGray),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No Image", color = Color.DarkGray)
                }
            }

            // Info Card
            Card(
                shape = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "🧾 Trash Type: ${type.replaceFirstChar { it.uppercase() }}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    // Determine the color based on bin type
                    val binColor = when (bin.lowercase()) {
                        "blue bin" -> Color(0xFF2196F3)   // Blue
                        "brown bin" -> Color(0xFF8D6E63)  // Brown
                        "orange bin" -> Color(0xFFFF9800) // Orange
                        "grey bin" -> Color.Gray          // Grey
                        else -> Color.Black
                    }

                    Text(
                        text = "🗑 Recycle Bin: $bin",
                        style = MaterialTheme.typography.bodyLarge,
                        color = binColor,
                        modifier = Modifier.clickable { showBinDialog = true }
                    )

                    Text(
                        text = "🌍 Dangerous: ${if (isDangerous) "✅ Yes" else "❌ No"}",
                        style = MaterialTheme.typography.bodyLarge,
                        color = if (isDangerous) Color.Red else Color.Gray
                    )
                }
            }

            // Bin Color Info Table
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("♻️ Malaysian Bin Color Guide:", style = MaterialTheme.typography.titleSmall)
                Spacer(Modifier.height(8.dp))
                Text("🔵 Blue: Paper (e.g. newspaper, boxes)")
                Text("🟤 Brown: Glass (e.g. bottles, jars)")
                Text("🟠 Orange: Plastic (e.g. containers, packaging)")
                Text("⚪️ Grey: Metal (e.g. cans, aluminium)")
            }
        }

        // Dialog: Bin Info Popup
        if (showBinDialog) {
            AlertDialog(
                onDismissRequest = { showBinDialog = false },
                confirmButton = {
                    TextButton(onClick = { showBinDialog = false }) {
                        Text("OK")
                    }
                },
                title = { Text("Bin Type: $bin") },
                text = {
                    // Get bin image resource id based on bin type
                    val binImageRes = when (bin.lowercase()) {
                        "blue bin" -> R.drawable.tongbiru
                        "brown bin" -> R.drawable.tongcoklat
                        "orange bin" -> R.drawable.tongoren
                        "grey bin" -> R.drawable.tonggrey
                        else -> null
                    }

                    val binInfo = when (bin.lowercase()) {
                        "blue bin" -> "Paper: Newspapers, magazines, cardboard, boxes."
                        "brown bin" -> "Glass: Bottles, jars."
                        "orange bin" -> "Plastic: Bottles, containers, plastic packaging."
                        "grey bin" -> "Metal: Cans, tins, aluminium items."
                        else -> "General waste or unclassified item."
                    }

                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        binImageRes?.let { res ->
                            Image(
                                painter = painterResource(id = res),
                                contentDescription = "$bin image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(150.dp),
                                contentScale = ContentScale.Fit
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }

                        Text(binInfo)
                    }
                }
            )
        }

    }
}





