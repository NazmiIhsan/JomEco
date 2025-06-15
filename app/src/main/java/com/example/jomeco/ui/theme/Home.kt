package com.example.jomeco.ui.theme

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
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
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material3.NavigationBar
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.jomeco.database.Event
import com.example.jomeco.viewModel.EventViewModel
import androidx.compose.runtime.getValue
import androidx.compose.ui.draw.clip


data class EventCard(
    val id: String,
    val imageRes: Int,
    val title: String,
    val location: String,
    val date: String
)


@Composable
fun HomeScreen(navController: NavController, modifier: Modifier = Modifier) {

    val context = LocalContext.current
    val sharedPref = context.getSharedPreferences("jomeco_prefs", Context.MODE_PRIVATE)
    val userId = sharedPref.getInt("current_user_id", -1)

    val viewModel: EventViewModel = viewModel()
    val events by viewModel.getEventsNotJoinedBy(userId).collectAsState(initial = emptyList())

    Scaffold(
        topBar = {
            TopBarHome(navController = navController)
        },
        bottomBar = {
            HomeBottomNavBar(navController = navController)
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            items(events) { event ->
                EventCard(
                    event = event,
                    onClick = {
                        navController.navigate("eventdetail/${event.id}")
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
fun EventCard(event: Event, onClick: () -> Unit) {
    val context = LocalContext.current
    val imageResId = remember(event.imageUrl) {
        context.resources.getIdentifier(
            event.imageUrl.removeSuffix(".jpg"), "drawable", context.packageName
        )
    }

    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
        modifier = Modifier
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            if (imageResId != 0) {
                Image(
                    painter = painterResource(id = imageResId),
                    contentDescription = event.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(12.dp))
                )
                Spacer(modifier = Modifier.height(12.dp))
            }

            Text(
                text = event.title,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "${event.date} • ${event.time}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Text(
                text = event.location,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Points: ${event.points}   |   Hours: ${event.hours}",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.secondary
            )
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










