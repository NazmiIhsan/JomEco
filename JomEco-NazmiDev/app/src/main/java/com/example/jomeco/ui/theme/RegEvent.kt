package com.example.jomeco.ui.theme

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController


@Composable
fun RegForm(navController: NavController, modifier: Modifier)
{
    Scaffold(
        topBar = {
            TopAppBar(navController = navController, "")
        },
        content = { paddingValues ->
            Column(
                modifier = modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {

                Spacer(modifier = Modifier.height(16.dp))

                eventReg(navController = navController, modifier = Modifier)

            }
        },





        )
}


@Composable
fun eventReg(navController: NavController,modifier: Modifier)
{
    var name by remember { mutableStateOf("") }
    var nric by remember { mutableStateOf("")}
    var ecna by remember { mutableStateOf("")}
    var email by remember { mutableStateOf("") }
    var pnumber by remember { mutableStateOf("") }
    var ecnu by remember { mutableStateOf("") }


    Column (modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = modifier.height(40.dp))

//        Full Name
        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Full Name") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),


            )


        Spacer(modifier = modifier.height(30.dp))

//        NRIC
        TextField(
            value = nric,
            onValueChange = { nric = it },
            label = { Text("NRIC") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Number),


            )

        Spacer(modifier = modifier.height(30.dp))

//        Email Address
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email),


            )

        Spacer(modifier = modifier.height(30.dp))

//        Phone Number
        TextField(
            value = pnumber,
            onValueChange = { pnumber = it },
            label = { Text("Phone Number") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),

            )

        Spacer(modifier = modifier.height(30.dp))

//        Emergency Contact Name
        TextField(
            value = ecna,
            onValueChange = { ecna = it },
            label = { Text("Emergency Contact Name") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),


            )

        Spacer(modifier = modifier.height(30.dp))

//        Emergency Contact Number
        TextField(
            value = ecnu,
            onValueChange = { ecnu = it },
            label = { Text("Emergency Contact Number") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done, keyboardType = KeyboardType.Number),


            )

        Spacer(modifier = modifier.height(30.dp))


        Button(
            onClick = {
                navController.navigate("home")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = Color.White)
        ) {

            Text(
                text = "Submit",
            )



        }



    }
}







