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
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.jomeco.R
import com.example.jomeco.database.Event
import com.example.jomeco.viewModel.EventViewModel

@Composable
fun Detail(navController: NavController, modifier: Modifier, eventId: Int) {
    val viewModel: EventViewModel = viewModel()
    val event by viewModel.getEventById(eventId).collectAsState(initial = null)

    var showDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(navController = navController, "Event Details")
        },
        floatingActionButton = {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.BottomCenter
            ) {
                ElevatedButton(
                    onClick = { navController.navigate("regform/${eventId}")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                ) {
                    Text("Join")
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { paddingValues ->

        if (event != null) {
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                Spacer(modifier = Modifier.height(16.dp))
                events(event = event!!, onImageClick = { showDialog = true })
            }
        } else {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        }

        if (showDialog && event != null) {
            val context = LocalContext.current
            val imageResId = remember(event!!.imageUrl) {
                context.resources.getIdentifier(event!!.imageUrl, "drawable", context.packageName)
            }

            AlertDialog(
                onDismissRequest = { showDialog = false },
                text = {
                    Image(
                        painter = painterResource(imageResId),
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
fun events(event: Event, onImageClick: () -> Unit) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        val context = LocalContext.current
        val imageResId = remember(event.imageUrl) {
            context.resources.getIdentifier(event.imageUrl, "drawable", context.packageName)
        }

        Image(
            painter = painterResource(imageResId),
            contentDescription = null,
            modifier = Modifier.clickable { onImageClick() }.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            event.title,
            style = MaterialTheme.typography.displayLarge,
            fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.tertiary
        )

        Spacer(modifier = Modifier.height(20.dp))

        Column(modifier = Modifier.padding(start = 10.dp)) {
            Text("📅 ${event.date}   ⏰ ${event.time}")
            Spacer(modifier = Modifier.height(10.dp))
            Text("📍 ${event.location}")
            Spacer(modifier = Modifier.height(10.dp))
            Text("🧾 Points: ${event.points} | Hours: ${event.hours}")
        }

        Spacer(modifier = Modifier.height(20.dp))


        Text(
            event.description,
            style = MaterialTheme.typography.displayLarge,
            fontSize = 20.sp,
//            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            color = MaterialTheme.colorScheme.tertiary
        )
    }
}


