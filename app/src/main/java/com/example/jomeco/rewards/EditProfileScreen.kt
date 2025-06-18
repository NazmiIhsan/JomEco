import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.text.input.PasswordVisualTransformation
import com.example.profile.ui.theme.ProfileViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    viewModel: ProfileViewModel,
    onSaveClicked: () -> Unit,
    onBackClicked: () -> Unit
) {
    var name by remember { mutableStateOf(viewModel.name) }
    var email by remember { mutableStateOf(viewModel.email) }
    var phone by remember { mutableStateOf(viewModel.pnumber) }

    var showSuccessMessage by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    var changePassword by remember { mutableStateOf(false) }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()


    LaunchedEffect(Unit) {
        viewModel.loadUserData()
    }

    val snackbarHostState = remember { SnackbarHostState() }
    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        text = "Edit Profile",
                        style = MaterialTheme.typography.displayLarge,
                        color = MaterialTheme.colorScheme.tertiary,
                        fontSize = 25.sp,
                        modifier = Modifier.padding(top = 10.dp)
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClicked) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color(0xFFE9E4BE),
                    titleContentColor = Color(0xFF3F6654),
                    navigationIconContentColor = Color(0xFF3F6654)
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Name") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = phone,
                onValueChange = { phone = it },
                label = { Text("Phone Number") },
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Change Password?")
                Spacer(modifier = Modifier.weight(1f))
                Switch(
                    checked = changePassword,
                    onCheckedChange = {
                        changePassword = it
                        if (!it) {
                            password = ""
                            confirmPassword = ""
                        }
                    }
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (changePassword) {
                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("New Password") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )


                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    label = { Text("Confirm Password") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium,
                    singleLine = true,
                    visualTransformation = PasswordVisualTransformation()
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            Button(
                onClick = {
                    when {
                        name.isBlank() || email.isBlank() || phone.isBlank() -> {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Please fill in all fields")
                            }
                        }
                        changePassword && (password.isBlank() || confirmPassword.isBlank()) -> {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Please enter both password fields")
                            }
                        }
                        changePassword && password != confirmPassword -> {
                            coroutineScope.launch {
                                snackbarHostState.showSnackbar("Passwords do not match")
                            }
                        }
                        else -> {
                            viewModel.updateUserProfile(
                                newName = name,
                                newEmail = email,
                                newPhone = phone,
                                newPassword = if (changePassword) password else null,
                                onSuccess = {
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar("Profile updated successfully")
                                    }
                                    onSaveClicked()
                                },
                                onError = { error ->
                                    coroutineScope.launch {
                                        snackbarHostState.showSnackbar(error)
                                    }
                                }
                            )
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Save")
            }


            Spacer(modifier = Modifier.height(16.dp))

            if (showSuccessMessage) {
                Text(
                    text = "Profile updated successfully!",
                    color = Color.Green,
                    fontWeight = FontWeight.Medium
                )
            }

            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}
