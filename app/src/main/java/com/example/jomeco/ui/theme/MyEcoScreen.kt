package com.example.jomeco.ui.theme

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import kotlin.math.min

data class VolunteerActivity(
    val id: String,
    val title: String,
    val date: LocalDate,
    val hours: Int,
    val description: String,
    val imageUri: Uri? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyEcoScreen(navController: NavController) {
    var activities by remember { mutableStateOf(listOf<VolunteerActivity>()) }
    var showAddDialog by remember { mutableStateOf(false) }
    var totalHours by remember { mutableStateOf(0) }
    var activityToEdit by remember { mutableStateOf<VolunteerActivity?>(null) }
    var showConfetti by remember { mutableStateOf(false) }
    val targetHours = 30

    // Animation for progress bar
    val progressAnimation = remember { Animatable(0f) }
    LaunchedEffect(totalHours) {
        val targetProgress = min(totalHours.toFloat() / targetHours, 1f)
        progressAnimation.animateTo(
            targetProgress,
            animationSpec = tween(
                durationMillis = 1000,
                easing = FastOutSlowInEasing
            )
        )

        // Show confetti when reaching target hours
        if (totalHours >= targetHours && !showConfetti) {
            showConfetti = true
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                navController = navController,
                topName = "My Eco"
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddDialog = true },
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = MaterialTheme.colorScheme.onTertiary
            ) {
                Icon(Icons.Default.Add, "Add Activity")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(MaterialTheme.colorScheme.background)
        ) {
            Column(
                modifier = Modifier.fillMaxSize()
            ) {
                // Total Hours Card with Progress
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            "Total Volunteer Hours Goals",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "$totalHours / $targetHours hours",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(16.dp))

                        // Progress Bar
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth(progressAnimation.value)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(MaterialTheme.colorScheme.primary)
                            )
                        }

                        // Progress Text
                        Text(
                            "${(progressAnimation.value * 100).toInt()}% Complete",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(top = 8.dp)
                        )

                        // Achievement Message
                        if (totalHours >= targetHours) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "🎉 Congratulations! You've reached your goal! 🎉",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                // Activities List
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(activities) { activity ->
                        ActivityCard(
                            activity = activity,
                            onDelete = {
                                activities = activities.filter { it.id != activity.id }
                                totalHours = activities.sumOf { it.hours }
                            },
                            onEdit = {
                                activityToEdit = activity
                                showAddDialog = true
                            }
                        )
                    }
                }
            }

            // Confetti Effect
            if (showConfetti) {
                ConfettiEffect(
                    modifier = Modifier.fillMaxSize(),
                    onFinish = { showConfetti = false }
                )
            }
        }

        if (showAddDialog) {
            AddActivityDialog(
                onDismiss = {
                    showAddDialog = false
                    activityToEdit = null
                },
                onAdd = { title, date, hours, description, imageUri ->
                    if (activityToEdit != null) {
                        activities = activities.map {
                            if (it.id == activityToEdit!!.id) {
                                VolunteerActivity(it.id, title, date, hours, description, imageUri)
                            } else it
                        }
                    } else {
                        val newActivity = VolunteerActivity(
                            id = System.currentTimeMillis().toString(),
                            title = title,
                            date = date,
                            hours = hours,
                            description = description,
                            imageUri = imageUri
                        )
                        activities = activities + newActivity
                    }
                    totalHours = activities.sumOf { it.hours }
                    showAddDialog = false
                    activityToEdit = null
                },
                initialActivity = activityToEdit
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActivityCard(
    activity: VolunteerActivity,
    onDelete: () -> Unit,
    onEdit: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Image Section
            activity.imageUri?.let { uri ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(
                            ImageRequest.Builder(LocalContext.current)
                                .data(uri)
                                .crossfade(true)
                                .build()
                        ),
                        contentDescription = "Activity Image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    activity.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Surface(
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier.padding(4.dp)
                ) {
                    Text(
                        "${activity.hours} hours",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    Icons.Default.DateRange,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    activity.date.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                activity.description,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(
                    onClick = onEdit,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.primary
                    )
                ) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Edit",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Edit")
                }
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(
                    onClick = onDelete,
                    colors = ButtonDefaults.textButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Delete")
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddActivityDialog(
    onDismiss: () -> Unit,
    onAdd: (title: String, date: LocalDate, hours: Int, description: String, imageUri: Uri?) -> Unit,
    initialActivity: VolunteerActivity? = null
) {
    var title by remember { mutableStateOf(initialActivity?.title ?: "") }
    var hours by remember { mutableStateOf(initialActivity?.hours?.toString() ?: "") }
    var description by remember { mutableStateOf(initialActivity?.description ?: "") }
    var selectedDate by remember { mutableStateOf(initialActivity?.date ?: LocalDate.now()) }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(initialActivity?.imageUri) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        selectedImageUri = uri
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                if (initialActivity != null) "Edit Activity" else "Add Volunteer Activity",
                style = MaterialTheme.typography.titleLarge
            )
        },
        text = {
            Column {
                // Image Selection
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surfaceVariant)
                        .clickable { imagePicker.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    if (selectedImageUri != null) {
                        Image(
                            painter = rememberAsyncImagePainter(
                                ImageRequest.Builder(LocalContext.current)
                                    .data(selectedImageUri)
                                    .crossfade(true)
                                    .build()
                            ),
                            contentDescription = "Selected Image",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                Icons.Default.AddAPhoto,
                                contentDescription = "Add Image",
                                modifier = Modifier.size(48.dp),
                                tint = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "Add Activity Image",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Activity Title") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = selectedDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                    onValueChange = { },
                    label = { Text("Date") },
                    modifier = Modifier.fillMaxWidth(),
                    readOnly = true,
                    trailingIcon = {
                        IconButton(onClick = { showDatePicker = true }) {
                            Icon(Icons.Default.DateRange, "Select Date")
                        }
                    },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = hours,
                    onValueChange = { hours = it },
                    label = { Text("Hours") },
                    modifier = Modifier.fillMaxWidth(),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
                Spacer(modifier = Modifier.height(16.dp))
                OutlinedTextField(
                    value = description,
                    onValueChange = { description = it },
                    label = { Text("Description") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = MaterialTheme.colorScheme.outline
                    )
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    val hoursInt = hours.toIntOrNull() ?: 0
                    if (title.isNotBlank() && hoursInt > 0) {
                        onAdd(title, selectedDate, hoursInt, description, selectedImageUri)
                    }
                }
            ) {
                Text(if (initialActivity != null) "Save" else "Add")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = { showDatePicker = false }) {
                    Text("OK")
                }
            }
        ) {
            DatePicker(
                state = rememberDatePickerState(initialSelectedDateMillis = selectedDate.toEpochDay() * 24 * 60 * 60 * 1000),
                title = { Text("Select Date") },
                headline = { Text("Select Date") },
                showModeToggle = false
            )
        }
    }
}

@Composable
fun ConfettiEffect(
    modifier: Modifier = Modifier,
    onFinish: () -> Unit
) {
    val particles = remember { List(100) { ConfettiParticle() } }
    var animationProgress by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        animate(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = tween(3000, easing = LinearEasing)
        ) { value, _ ->
            animationProgress = value
        }
        onFinish()
    }

    Box(modifier = modifier) {
        particles.forEach { particle ->
            val x = particle.initialX + (particle.velocityX * animationProgress * 1000)
            val y = particle.initialY + (particle.velocityY * animationProgress * 1000) +
                   (0.5f * 9.8f * animationProgress * animationProgress * 1000)
            val rotation = particle.initialRotation + (particle.rotationSpeed * animationProgress * 360)

            Box(
                modifier = Modifier
                    .offset(x = x.dp, y = y.dp)
                    .size(8.dp)
                    .rotate(rotation)
                    .background(
                        color = particle.color,
                        shape = RoundedCornerShape(2.dp)
                    )
            )
        }
    }
}

data class ConfettiParticle(
    val initialX: Float = Random().nextFloat() * 1000 - 500,
    val initialY: Float = -100f,
    val velocityX: Float = Random().nextFloat() * 200 - 100,
    val velocityY: Float = Random().nextFloat() * -200 - 100,
    val initialRotation: Float = Random().nextFloat() * 360,
    val rotationSpeed: Float = Random().nextFloat() * 2 - 1,
    val color: Color = listOf(
        Color(0xFFFFD700), // Gold
        Color(0xFFFF69B4), // Pink
        Color(0xFF00BFFF), // Blue
        Color(0xFF32CD32), // Green
        Color(0xFFFF4500)  // Orange
    ).random()
)