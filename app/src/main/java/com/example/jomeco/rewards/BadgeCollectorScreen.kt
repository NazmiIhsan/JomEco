package com.example.profile.ui.theme
import androidx.compose.foundation.Image
import com.example.jomeco.rewards.Badge
import androidx.compose.foundation.background
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.profile.viewmodel.BadgeViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BadgeCollectorScreen(
    viewModel: BadgeViewModel,
    onBackClick: () -> Unit = {},
    userId: Int
) {
    val badges by viewModel.badges
    val isLoading by viewModel.isLoading
    val userPoints by viewModel.userPoints
    val snackbarHostState = remember { SnackbarHostState() }


    val errorMessage = viewModel.errorMessage.value
    val successMessage = viewModel.successMessage.value

    if (errorMessage != null) {
        LaunchedEffect(errorMessage) {
            snackbarHostState.showSnackbar(errorMessage)
            viewModel.clearErrorMessage()
        }
    }
    if (successMessage != null) {
        LaunchedEffect(successMessage) {
            snackbarHostState.showSnackbar(successMessage)
            viewModel.clearSuccessMessage()
        }
    }

    LaunchedEffect(userId) {
        viewModel.loadBadgesForUser(userId)
    }


    if (isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                color = Color(0xFF3F6654)
            )
        }
        return
    }

    val earnedBadges = badges.filter { it.isEarned }
    val availableBadges = badges.filter { !it.isEarned }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Badge Collector",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 30.sp,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                // Stats Row
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    StatCard(
                        title = "Earned",
                        value = earnedBadges.size.toString(),
                        color = Color(0xFF4CAF50)
                    )
                    StatCard(
                        title = "Available",
                        value = availableBadges.size.toString(),
                        color = Color(0xFF2196F3)
                    )
                    StatCard(
                        title = "My Points",
                        value = userPoints.toString(),
                        color = Color(0xFFFF9800)
                    )
                }

                LazyColumn(
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Earned Badges Section
                    if (earnedBadges.isNotEmpty()) {
                        item {
                            SectionHeader("Earned Badges")
                        }
                        items(earnedBadges) { badge ->
                            BadgeCard(
                                badge = badge,
                                onClaimClick = {
                                    // Already earned; do nothing
                                }
                            )
                        }
                    }

                    // Available Badges Section
                    if (availableBadges.isNotEmpty()) {
                        item {
                            SectionHeader("Available Badges")
                        }
                        items(availableBadges) { badge ->
                            BadgeCard(
                                badge = badge,
                                onClaimClick = {
                                    viewModel.claimBadge(badge.id)
                                }
                            )
                        }
                    }

                    // Empty state
                    if (availableBadges.isEmpty() && earnedBadges.isEmpty()) {
                        item {
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(32.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White.copy(alpha = 0.9f)
                                )
                            ) {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(24.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Icon(
                                        Icons.Default.EmojiEvents,
                                        contentDescription = "No badges",
                                        modifier = Modifier.size(48.dp),
                                        tint = Color.Gray
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "No badges available yet",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = Color.Gray
                                    )
                                    Text(
                                        text = "Check back later for new challenges!",
                                        fontSize = 14.sp,
                                        color = Color.Gray,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    )
}


@Composable
fun StatCard(
    title: String,
    value: String,
    color: Color
) {
    Surface(
        modifier = Modifier
            .size(80.dp)
            .shadow(4.dp, CircleShape),
        shape = CircleShape,
        color = Color.White
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = value,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Text(
                text = title,
                fontSize = 10.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                maxLines = 1
            )
        }
    }
}

@Composable
fun SectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 18.sp,
        fontWeight = FontWeight.Bold,
        color = Color(0xFF3F6654),
        modifier = Modifier.padding(vertical = 8.dp)
    )
}

@Composable
fun BadgeCard(
    badge: Badge,
    onClaimClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = Color.White
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Badge Icon
            Surface(
                modifier = Modifier.size(60.dp),
                shape = CircleShape,
                color = badge.color.copy(alpha = 0.1f)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = badge.iconResId),
                        contentDescription = badge.name,
                        modifier = Modifier
                            .size(90.dp) // Adjust this if you want bigger/smaller icon
                    )
                }
            }


            Spacer(modifier = Modifier.width(16.dp))

            // Badge Info
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = badge.name,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    if (badge.isEarned) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            Icons.Default.CheckCircle,
                            contentDescription = "Earned",
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

//                Text(
//                    text = badge.description,
//                    fontSize = 12.sp,
//                    color = Color.Gray,
//                    modifier = Modifier.padding(top = 4.dp)
//                )

                Text(
                    text = "Point: ${badge.pointsReward}",
                    fontSize = 15.sp,
                    color = Color(0xFF3F6654),
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 2.dp)
                )

                if (badge.isEarned && badge.dateEarned != null) {
                    Text(
                        text = "Earned: ${badge.dateEarned}",
                        fontSize = 10.sp,
                        color = Color.Gray,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }
            }

            // Points and Action
            Column(
                horizontalAlignment = Alignment.End
            ) {
//                Surface(
//                    color = badge.color.copy(alpha = 0.1f),
//                    shape = RoundedCornerShape(8.dp)
//                ) {
//                    Text(
//                        text = "+${badge.pointsReward}",
//                        fontSize = 12.sp,
//                        fontWeight = FontWeight.Bold,
//                        color = badge.color,
//                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
//                    )
//                }


                Spacer(modifier = Modifier.height(8.dp))

                if (badge.isEarned) {
                    Surface(
                        color = Color(0xFF4CAF50).copy(alpha = 0.1f),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "CLAIMED",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF4CAF50),
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                        )
                    }
                } else {
                    Button(
                        onClick = onClaimClick,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF3F6654)
                        ),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.height(32.dp)
                    ) {
                        Text(
                            text = "CLAIM",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

