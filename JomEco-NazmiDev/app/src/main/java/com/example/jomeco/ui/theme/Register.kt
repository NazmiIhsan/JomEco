package com.example.jomeco.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.jomeco.R


@Composable
fun Register(navController: NavController, modifier: Modifier)
{
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
//        modifier = Modifier.padding(top = 40.dp)
    ) {
        Text("Register",
            style = MaterialTheme.typography.displayLarge,
            color = MaterialTheme.colorScheme.tertiary,
            modifier = Modifier.padding(top = 40.dp)
        )


        informasi(navController = navController, modifier = Modifier)

    }

    Box(modifier = modifier.fillMaxSize())
    {
        IconButton(
            onClick = {navController.popBackStack()},
            modifier = Modifier.align(Alignment.TopStart).padding(16.dp).padding(top = 23.dp)
        ) {

            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.tertiary
            )
        }
    }
}



@Composable
fun informasi(navController: NavController,modifier: Modifier)
{

    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("")}
    var cpassword by remember { mutableStateOf("")}
    var email by remember { mutableStateOf("") }
    var pnumber by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("") }


    Column (modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Spacer(modifier = modifier.height(40.dp))

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Username") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),


            )

        Spacer(modifier = modifier.height(30.dp))

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next, keyboardType = KeyboardType.Email),


            )

        Spacer(modifier = modifier.height(30.dp))

        TextField(
            value = pnumber,
            onValueChange = { pnumber = it },
            label = { Text("Phone Number") },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),

            )

        Spacer(modifier = modifier.height(30.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),

            )

        Spacer(modifier = modifier.height(30.dp))

        TextField(
            value = cpassword,
            onValueChange = { cpassword = it },
            label = { Text("Confirm Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Next
            ),

            )

        Spacer(modifier = modifier.height(30.dp))

        DropDown(
            selectedCategory = selectedCategory,
            onCategorySelected = { selectedCategory = it },
            modifier = Modifier
        )

        Spacer(modifier = modifier.height(30.dp))

        Button(
            onClick = {
                navController.navigate("login")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = Color.White)
        ) {

            Text(
                text = "Sign Up",
            )



        }



    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDown(
    selectedCategory: String,
    onCategorySelected: (String) -> Unit,
    modifier: Modifier
) {
    val list = listOf("Volunteer", "Organization")
    val LightBlue = Color(0xFFADD8E6)
    var isExpanded by remember { mutableStateOf(false) }

    Column() {
        ExposedDropdownMenuBox(
            expanded = isExpanded,
            onExpandedChange = { isExpanded = !isExpanded }
        ) {
            TextField(
                value = selectedCategory,
                onValueChange = {},
                readOnly = true,
                label = { Text("Choose Role") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded) },
                modifier = Modifier.menuAnchor()

            )

            ExposedDropdownMenu(
                expanded = isExpanded,
                onDismissRequest = { isExpanded = false }
            ) {
                list.forEach { text ->
                    DropdownMenuItem(
                        text = { Text(text) },
                        onClick = {
                            onCategorySelected(text)
                            isExpanded = false
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}