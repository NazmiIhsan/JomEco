package com.example.jomeco.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jomeco.R
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.filled.Article
import androidx.compose.material.icons.filled.Eco
import androidx.compose.material.icons.filled.Notes
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.NavigationBar
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jomeco.LogIn


data class EventCard(
    val id: String,
    val imageRes: Int,
    val title: String,
    val location: String,
    val date: String
)


@Composable
fun HomeScreen(navController: NavController, modifier: Modifier) {

    val dummyEco = listOf(
        EventCard("eco1",R.drawable.eco1, "#Keluarga Malaysia Run - Earth Day 2022: Invest in Our Planet & Combat Climate Change.",
            "Kuala Lumpur", "24 Sept"),
        EventCard("eco2",R.drawable.eco2, "Malaysia FutureGreens Expo", "Port Dickson", "1 July"),
        EventCard("eco3",R.drawable.eco3, "Program Tanam 1 Juta Pokok Sehari", "Pulau Pinang", "22 April")
    )



    Scaffold(
        topBar = {
            TopBarHome(navController = navController, modifier = Modifier.statusBarsPadding())
        },

        bottomBar = {HomeBottomNavBar(navController = navController)}
    ) { paddingValues ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(dummyEco) { eco ->
                InfoCard(
                    imageRes = eco.imageRes,
                    title = eco.title,
                    location = eco.location,
                    date = eco.date,
                    onClick = {
                        navController.navigate("eventdetail/${eco.id}")
                    }
                )
            }
        }

    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarHome(navController: NavController, modifier: Modifier = Modifier) {


    CenterAlignedTopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.Absolute.Center
            ) {
                Image(
                    painter = painterResource(R.drawable.eco),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.size(60.dp)
                )

                Text("JomEco",
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(top = 10.dp))

            }


        },

        actions = {
            IconButton(onClick = { /* TODO: Handle search */ }) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search",
                    tint = MaterialTheme.colorScheme.tertiary
                )
            }
        },

        modifier = modifier,
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer)

    )

}



@Composable
fun InfoCard(imageRes: Int, title: String, location: String, date: String, onClick: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(8.dp),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .clickable{ onClick()}
    ) {
        Column {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = title, style = MaterialTheme.typography.titleMedium)
                Text(text = "\uD83D\uDCCD $location", style = MaterialTheme.typography.bodyMedium)
                Text(text = "\uD83D\uDCC5 $date", style = MaterialTheme.typography.bodySmall)
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
            BottomNavItem("Home", Icons.Filled.Home)
            {

            }
            BottomNavItem("My Eco", Icons.Filled.Eco)
            {
                navController.navigate("myeco")
            }
            BottomNavItem("Eco Scan", Icons.Filled.QrCodeScanner)
            {
                navController.navigate("ecoscan")
            }
            BottomNavItem("Eco Facts", Icons.Filled.Article)
            {

            }
            BottomNavItem("Profile", Icons.Default.Person)
            {

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










