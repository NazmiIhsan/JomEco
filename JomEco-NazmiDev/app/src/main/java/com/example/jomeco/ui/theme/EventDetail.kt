package com.example.jomeco.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.jomeco.R

@Composable
fun Detail(navController: NavController, modifier: Modifier, eventId: String) {
var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(navController = navController, "Event Details")
        },
        content = { paddingValues ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                Spacer(modifier = Modifier.height(16.dp))
                events(modifier = Modifier, onImageClick = {showDialog= true})
            }
        },

        floatingActionButton = {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                ElevatedButton(
                    onClick = { navController.navigate("regform") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, end = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                ) {
                    Text("Join")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center,



    )

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            text = {
                Image(
                    painter = painterResource(R.drawable.eco1),
                    contentDescription = null,
                    contentScale = ContentScale.Fit,
                    modifier = Modifier.fillMaxWidth()
                )
            },
            confirmButton = {
                TextButton(onClick = { showDialog = false }) {
                    Text("Close")
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(navController: NavController, topName: String) {
    CenterAlignedTopAppBar(
        title = {
            Row(
                horizontalArrangement = Arrangement.Absolute.Center
            ) {


                Text(topName,
                    style = MaterialTheme.typography.displayLarge,
                    color = MaterialTheme.colorScheme.tertiary,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(top = 10.dp))

            }


        },

        navigationIcon = {
            IconButton(
                onClick = {navController.popBackStack()},
            ) {

                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.Black
                )
            }
        },
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer)

    )
}

@Composable
fun events(modifier: Modifier, onImageClick: () -> Unit) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(start = 16.dp, end = 16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.eco1),
            contentDescription = null,
            modifier = Modifier.clickable{onImageClick()}
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            "#Keluarga Malaysia Run - Earth Day 2022 - Kuala Lumpur",
            style = MaterialTheme.typography.displayLarge,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.tertiary
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(modifier = Modifier.padding(start = 10.dp)) {
            Row {
                Text(
                    text = "\uD83D\uDCC5 24 September 2022 (Saturday) | 08:00 AM",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(
                    text = "\uD83D\uDCCD DBKL, Jalan Raja Laut",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row {
                Text(
                    text = "\uD83D\uDCDC Certificate For All Participants",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        Spacer(modifier = Modifier.height(30.dp))

        Text(
            "About",
            style = MaterialTheme.typography.displayLarge,
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.padding(start = 10.dp),
            textDecoration = TextDecoration.Underline
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "#KELUARGA MALAYSIA RUN, a running event held on September 24, 2022, at Jalan Raja Laut in Kuala Lumpur, Malaysia. Featuring 3km and 10km categories, this run was organized under the \"Keluarga Malaysia\" national initiative and aimed to raise awareness about critical environmental issues, prominently themed around \"Climate Change\" and \"Earth Day 2022: Invest in Our Planet.\" The event saw involvement from Malaysian government bodies, such as the Ministry of National Unity, alongside sports event organizers, bringing the community together for a cause.",
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp)
        )

        Spacer(modifier = Modifier.height(70.dp))

    }
}
