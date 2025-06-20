package com.example.jomeco

import EditProfileScreen
import android.app.Application
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.jomeco.ui.theme.JomEcoTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.jomeco.database.AppDatabase
import com.example.jomeco.database.hashPassword
import com.example.jomeco.ui.theme.Detail
import com.example.jomeco.ui.theme.HomeScreen
import com.example.jomeco.ui.theme.ProductDetail
import com.example.jomeco.ui.theme.RegForm
import com.example.jomeco.ui.theme.Register
import com.example.jomeco.ui.theme.Scan
import com.example.jomeco.ui.theme.ScanViewModel
import com.example.jomeco.ui.theme.SplashScreen
import com.example.jomeco.viewModel.EventViewModel
import com.example.profile.ui.theme.BadgeCollectorScreen
//import com.example.profile.ui.theme.EditProfileScreen
import com.example.profile.ui.theme.ProfileScreen
import com.example.profile.ui.theme.ProfileViewModel
import com.example.profile.ui.theme.RewardsPage
import com.example.profile.viewmodel.BadgeViewModel
import kotlinx.coroutines.launch


class MainActivity : ComponentActivity() {
//    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val viewModel = ViewModelProvider(this)[EventViewModel::class.java]
//        viewModel.insertSampleEvent() // This runs once and inserts manually
        enableEdgeToEdge()
        setContent {
            JomEcoTheme {
                Surface(modifier = Modifier.fillMaxSize()) {

                    val navController = rememberNavController()
                    val scanViewModel = viewModel<ScanViewModel>(
                        viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
                    )
                    val profileViewModel: ProfileViewModel = viewModel()


                    NavHost(navController = navController, startDestination = "splash")
                        {
                        composable("splash") {
                            SplashScreen(navController = navController)
                        }

                            composable("login") {
                                LogIn(navController = navController)
                            }


                            composable("register") {
                                Register(navController = navController, modifier = Modifier)
                            }

                            composable("home") {
                                HomeScreen(navController = navController, modifier = Modifier)
                            }

                            composable("eventdetail/{eventId}") { backStackEntry ->
                                val eventId = backStackEntry.arguments?.getString("eventId")?.toIntOrNull() ?: 0
                                Detail(navController = navController, modifier = Modifier, eventId = eventId )
                            }


                            composable("regform/{eventId}") { backStackEntry ->
                                val eventId = backStackEntry.arguments?.getString("eventId")?.toIntOrNull() ?: 0
                                RegForm(navController = navController, modifier = Modifier, eventId = eventId)
                            }


                            composable("ecoscan") {
                                Scan(navController = navController,scanViewModel = scanViewModel)
                            }


                            composable("scanresult/{type}/{bin}/{danger}") { backStackEntry ->
                                val type = backStackEntry.arguments?.getString("type") ?: "unknown"
                                val bin = backStackEntry.arguments?.getString("bin") ?: "Unknown"
                                val danger = backStackEntry.arguments?.getString("danger")?.toBoolean() ?: false

                                val scanViewModel = viewModel<ScanViewModel>(
                                    viewModelStoreOwner = LocalContext.current as ViewModelStoreOwner
                                )
                                val bitmap by scanViewModel.bitmap.collectAsState()

                                ProductDetail(type, bin, danger, bitmap, navController)
                            }


                            composable("ProfileScreen") {
                                ProfileScreen(
                                    navController = navController,
                                    viewModel = profileViewModel, // Now it’s valid
                                    onEditClick = { navController.navigate("editProfile") },
                                    onRewardsClick = { navController.navigate("rewards") }
                                )
                            }

                            composable("rewards") {
                                RewardsPage(
                                    onBackClick = { navController.popBackStack() },
                                    onNavigateToBadges = { navController.navigate("badges") }
                                )
                            }

                            composable("badges") {
                                val context = LocalContext.current
                                val appContext = context.applicationContext as Application

                                val badgeViewModel = viewModel<BadgeViewModel>(
                                    factory = ViewModelProvider.AndroidViewModelFactory.getInstance(appContext)
                                )

                                // Get userId from SharedPreferences
                                val sharedPref = context.getSharedPreferences("jomeco_prefs", Context.MODE_PRIVATE)
                                val userId = sharedPref.getInt("current_user_id", -1)

                                if (userId != -1) {
                                    BadgeCollectorScreen(
                                        viewModel = badgeViewModel,
                                        userId = userId,
                                        onBackClick = { navController.popBackStack() }
                                    )
                                } else {
                                    // Optional: handle jika userId belum ada
                                    Text("User not found.")
                                }
                            }


                            composable("editProfile") {
                                EditProfileScreen(
                                    viewModel = profileViewModel,
                                    onSaveClicked = {
                                        navController.popBackStack()
                                    },
                                    onBackClicked = { navController.popBackStack()}
                                )
                            }















                        }


                }
            }
        }
    }
}

@Composable
fun LogIn(navController: NavController,modifier: Modifier = Modifier) {

    var name by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("")}
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    Column (modifier = modifier.padding(top = 30.dp).fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.jomeco),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = modifier.size(120.dp).clip(CircleShape)
        )

        Spacer(modifier = modifier.height(50.dp))

        TextField(
            value = name,
            onValueChange = {name = it},
            label = {Text("Email Address")},
            keyboardOptions = KeyboardOptions( imeAction = ImeAction.Next, keyboardType = KeyboardType.Email),


            )

        Spacer(modifier = modifier.height(30.dp))

        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password")},
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Done),

        )

        Spacer(modifier = modifier.height(30.dp))

        Button(
            onClick = {
                val hashedPassword = hashPassword(password)

                scope.launch {
                    try {
                        val db = AppDatabase.getDatabase(context)
                        // Check user by email and hashed password
                        val user = db.userDao().getUserByEmailAndPassword(name, hashedPassword)

                        if (user != null) {
                            val sharedPref = context.getSharedPreferences("jomeco_prefs", Context.MODE_PRIVATE)
                            sharedPref.edit().putInt("current_user_id", user.id).apply()

                            Toast.makeText(context, "Login Successful", Toast.LENGTH_SHORT).show()
                            navController.navigate("home")
                        } else {
                            Toast.makeText(context, "Invalid email or password", Toast.LENGTH_SHORT).show()
                        }
                    } catch (e: Exception) {
                        Toast.makeText(context, "Login failed: ${e.message}", Toast.LENGTH_LONG).show()
                    }
                }
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.tertiary,
                contentColor = Color.White
            )
        ) {
            Text(text = "Login")
        }

        Spacer(modifier = modifier.height(30.dp))


        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("Don't have an account?")

            TextButton(
                onClick = {
                    navController.navigate("register")
                },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Sign up")
            }
        }



    }
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    JomEcoTheme {
//        LogIn()
//    }
//}