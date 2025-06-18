package com.example.profile.ui.theme

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.jomeco.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    navController: NavController,
    viewModel: ProfileViewModel,
    onEditClick: () -> Unit,
    onRewardsClick: () -> Unit,
    onBackClick: () -> Unit = {},
) {
    val name = viewModel.name
    val profileImageUri = viewModel.profileImageUri



    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            viewModel.updateProfileImage(it)
        }
    }

    LaunchedEffect(Unit) {
        viewModel.loadUserProfile()
    }
    val snackbarHostState = remember { SnackbarHostState() }
    val showMessage = viewModel.showSuccessMessage
    LaunchedEffect(showMessage) {
        if (showMessage) {
            snackbarHostState.showSnackbar("Profile picture updated successfully!")
            viewModel.showSuccessMessage = false
        }
    }


    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            HomeBottomNavBar(navController)
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        "Profile",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 30.sp,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer
                )
            )
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier
                    .size(150.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                ProfileImage(profileImageUri.toString())

                IconButton(
                    onClick = { launcher.launch("image/*") },
                    modifier = Modifier
                        .size(36.dp)
                        .background(Color.White, CircleShape)
                        .border(1.dp, Color.Gray, CircleShape)
                        .padding(4.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Edit, // You can use Icons.Default.Edit or Icons.Default.Add
                        contentDescription = "Edit Profile Picture",
                        tint = Color(0xFF3F6654),
                        modifier = Modifier.size(20.dp)
                    )
                }
            }


            Text(
                text = name,
                color = Color.Black,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onEditClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3F6654),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
            ) {
                Text("Edit Profile", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

//            Spacer(modifier = Modifier.height(20.dp))
//
//            Button(
//                onClick = { /* Settings action */ },
//                colors = ButtonDefaults.buttonColors(
//                    containerColor = Color(0xFF3F6654),
//                    contentColor = Color.White
//                ),
//                shape = RoundedCornerShape(10.dp),
//                modifier = Modifier
//                    .fillMaxWidth(0.8f)
//                    .height(50.dp)
//            ) {
//                Text("Settings", fontSize = 18.sp, fontWeight = FontWeight.Bold)
//            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onRewardsClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3F6654),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
            ) {
                Text("Rewards", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = {
                    navController.navigate("login")
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3F6654),
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth(0.8f)
                    .height(50.dp)
            ) {
                Text("Log Out", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun HomeBottomNavBar(navController: NavController) {
    NavigationBar(
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        contentColor = MaterialTheme.colorScheme.tertiary
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            BottomNavItem("Home", Icons.Filled.Home) {
                navController.navigate("home")
            }
            BottomNavItem("My Eco", Icons.Filled.Eco) { }
            BottomNavItem("Eco Scan", Icons.Filled.QrCodeScanner) {
                navController.navigate("ecoscan")
            }
            BottomNavItem("Eco Facts", Icons.Filled.Article) { }
            BottomNavItem("Profile", Icons.Default.Person) {
                navController.navigate("ProfileScreen")
            }
        }
    }
}

@Composable
fun BottomNavItem(label: String, icon: ImageVector, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clickable { onClick() }
            .padding(8.dp)
    ) {
        Icon(imageVector = icon, contentDescription = label)
        Text(text = label, fontSize = 12.sp)
    }
}

@Composable
fun ProfileImage(imageUri: String?) {
    val model = if (imageUri.isNullOrEmpty()) {
        R.drawable.profile
    } else {
        ImageRequest.Builder(LocalContext.current)
            .data(imageUri)
            .crossfade(true)
            .build()
    }

    AsyncImage(
        model = model,
        contentDescription = "Profile Picture",
        modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)
            .border(2.dp, Color.Gray, CircleShape),
        contentScale = ContentScale.Crop
    )

}
