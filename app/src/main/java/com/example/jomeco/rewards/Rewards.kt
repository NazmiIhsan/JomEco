package com.example.profile.ui.theme



import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountTree
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Payment
import androidx.compose.material.icons.filled.LocalOffer
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Man
import androidx.compose.material.icons.filled.Numbers
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// Data classes
data class RewardPartner(
    val id: String,
    val name: String,
    val points: Int,
    val icon: ImageVector,
    val backgroundColor: Color,
    val isPopular: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardsPage(
    onBackClick: () -> Unit = {},
    onRewardClick: (RewardPartner) -> Unit = {},
    onNavigateToBadges: () -> Unit = {}
) {
    val rewardPartners = listOf(
        RewardPartner("1", "Badge Collector", 1200, Icons.Default.Badge, Color(0xFF4CAF50)),
        RewardPartner("2", "Points", 1200, Icons.Default.Numbers, Color(0xFF2196F3), true),
        RewardPartner("3", "Shoppers Stop", 1200, Icons.Default.ShoppingCart, Color(0xFF9C27B0)),
        RewardPartner("4", "Entertainment", 1200, Icons.Default.Star, Color(0xFFFF9800)),
        RewardPartner("5", "Fashion", 1200, Icons.Default.Man, Color(0xFFE91E63)),
        RewardPartner("6", "Travel", 1200, Icons.Default.LocationOn, Color(0xFFFF5722))
    )

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Rewards",
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
                            tint = Color(0xFF3F6654)
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)

        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "PICK YOUR REWARDS",
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.tertiary,
                fontSize = 20.sp,
                modifier = Modifier.padding(horizontal = 24.dp),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(24.dp))

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(rewardPartners) { partner ->
                    RewardCard(
                        partner = partner,
                        onClick = {
                            if (partner.name == "Badge Collector") {
                                onNavigateToBadges()
                            } else {
                                onRewardClick(partner)
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun RewardCard(
    partner: RewardPartner,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp)
            .shadow(4.dp, RoundedCornerShape(16.dp)),
        shape = RoundedCornerShape(16.dp),
        color = Color.White
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Background gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                // Popular badge
                if (partner.isPopular) {
                    Box(
                        modifier = Modifier
                            .background(
                                Color(0xFFFF4444),
                                RoundedCornerShape(8.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "POPULAR",
                            color = Color.White,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    Spacer(modifier = Modifier.height(16.dp))
                }

                // Icon
                Icon(
                    imageVector = partner.icon,
                    contentDescription = partner.name,
                    tint = partner.backgroundColor,
                    modifier = Modifier.size(32.dp)
                )

                // Partner name
                Text(
                    text = partner.name,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black.copy(alpha = 0.8f),
                    textAlign = TextAlign.Center,
                    maxLines = 1
                )

                // Points
                Box(
                    modifier = Modifier
                        .background(
                            Color(0xFF2196F3).copy(alpha = 0.1f),
                            RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${partner.points} PENNYS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2196F3)
                    )
                }
            }
        }
    }
}

// Preview
@Composable
fun RewardsPagePreview() {
    MaterialTheme {
        RewardsPage()
    }
}

