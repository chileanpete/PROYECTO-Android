package com.example.proyecto_droid

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.widget.Toast
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.proyecto_droid.R
import com.example.proyecto_droid.ui.screens.LoginScreen
import com.example.proyecto_droid.ui.screens.RegisterScreen
import com.example.proyecto_droid.ui.screens.HomeScreen
import com.example.proyecto_droid.ui.screens.NutritionScreen
import com.example.proyecto_droid.ui.screens.ExerciseScreen
import com.example.proyecto_droid.ui.screens.ProfileScreen
import com.example.proyecto_droid.ui.screens.EditProfileScreen
import com.example.proyecto_droid.ui.screens.EditPreferencesScreen
import com.example.proyecto_droid.ui.screens.ChangePasswordScreen
import com.example.proyecto_droid.ui.screens.LocalDetailScreen
import com.example.proyecto_droid.ui.viewmodel.SessionViewModel
import com.example.proyecto_droid.ui.viewmodel.SessionEvent
import com.example.proyecto_droid.ui.viewmodel.TallerViewModel
import com.example.proyecto_droid.ui.viewmodel.LugaresViewModel
import com.example.proyecto_droid.ui.viewmodel.LugaresViewModelFactory
import com.example.proyecto_droid.ui.viewmodel.LugaresUiState
import com.example.proyecto_droid.ui.theme.BackgroundLight
import com.example.proyecto_droid.ui.theme.GreenPrimary
import com.example.proyecto_droid.ui.theme.LightGrayText
import com.example.proyecto_droid.ui.theme.Proyecto_droidTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Proyecto_droidTheme {
                val navController = rememberNavController()
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = BackgroundLight
                ) {
                    AppNavHost(navController)
                }
            }
        }
    }
}

@Composable
fun AppNavHost(navController: NavHostController) {
    val sessionViewModel: SessionViewModel = androidx.lifecycle.viewmodel.compose.viewModel()
    val sessionState by sessionViewModel.uiState.collectAsStateWithLifecycle()
    
    // Observar cambios en el estado de sesión solo cuando no está cargando
    LaunchedEffect(sessionState.isLoggedIn, sessionState.isLoading) {
        if (!sessionState.isLoading) {
            if (sessionState.isLoggedIn) {
                navController.navigate("main") {
                    popUpTo("splash") { inclusive = true }
                }
            } else {
                navController.navigate("login") {
                    popUpTo("splash") { inclusive = true }
                }
            }
        }
    }
    
    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") { 
            SplashScreen() 
        }
        composable("login") { 
            LoginScreen(navController, sessionViewModel) 
        }
        composable("register") { 
            RegisterScreen(navController, sessionViewModel) 
        }
        composable("forgot") { 
            ForgotPasswordScreen(navController) 
        }
        composable("main") { 
            MainScaffold(sessionViewModel)
        }
    }
}

@Composable
fun SplashScreen() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(GreenPrimary),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Logo o icono de la app
            Icon(
                painter = painterResource(id = R.drawable.ic_launcher_foreground),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp),
                tint = Color.White
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                text = "Vida Sana UCSC",
                fontSize = 28.sp,
                color = Color.White,
                style = androidx.compose.material3.MaterialTheme.typography.headlineMedium
            )
            
            Spacer(modifier = Modifier.height(32.dp))
            
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(32.dp)
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            Text(
                text = "Cargando...",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f),
                style = androidx.compose.material3.MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(androidx.compose.material3.ExperimentalMaterial3Api::class)
@Composable
fun MainScaffold(sessionViewModel: SessionViewModel) {
    val navController = rememberNavController()
    var selectedIndex by remember { mutableStateOf(0) }
    val screens = listOf("home", "nutrition", "exercise", "profile")

    Scaffold(
        containerColor = BackgroundLight,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (selectedIndex) {
                            0 -> "Inicio"
                            1 -> "Comida"
                            2 -> "Ejercicio"
                            3 -> "Perfil"
                            else -> ""
                        },
                        color = Color.White
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = GreenPrimary
                ),
                modifier = Modifier.height(56.dp)
            )
        },
        bottomBar = {
            NavigationBar(containerColor = BackgroundLight) {
                NavigationBarItem(
                    selected = selectedIndex == 0,
                    onClick = { selectedIndex = 0; navController.navigate("home") },
                    icon = { Icon(painterResource(id = R.drawable.ic_launcher_foreground), contentDescription = "Home") },
                    label = { Text("Inicio") }
                )
                NavigationBarItem(
                    selected = selectedIndex == 1,
                    onClick = { selectedIndex = 1; navController.navigate("nutrition") },
                    icon = { Icon(painterResource(id = R.drawable.ic_nutrition), contentDescription = "Comida") },
                    label = { Text("Comida") }
                )
                NavigationBarItem(
                    selected = selectedIndex == 2,
                    onClick = { selectedIndex = 2; navController.navigate("exercise") },
                    icon = { Icon(painterResource(id = R.drawable.ic_exercise), contentDescription = "Ejercicio") },
                    label = { Text("Ejercicio") }
                )
                NavigationBarItem(
                    selected = selectedIndex == 3,
                    onClick = { selectedIndex = 3; navController.navigate("profile") },
                    icon = { Icon(painterResource(id = R.drawable.ic_profile), contentDescription = "Perfil") },
                    label = { Text("Perfil") }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "home",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("home") { 
                val tallerViewModel: TallerViewModel = viewModel()
                HomeScreen(navController, sessionViewModel, tallerViewModel) 
            }
            composable("nutrition") { NutritionScreen(navController, sessionViewModel) }
            composable("exercise") { ExerciseScreen(navController, sessionViewModel) }
            composable("profile") { 
                ProfileScreen(
                    onNavigateToEditProfile = { navController.navigate("edit_profile") },
                    onNavigateToEditPreferences = { navController.navigate("edit_preferences") },
                    onNavigateToChangePassword = { navController.navigate("change_password") },
                    onLogout = { 
                        // Usar sessionViewModel para hacer logout
                        sessionViewModel.handleEvent(SessionEvent.Logout)
                    }
                )
            }
            composable("edit_profile") {
                EditProfileScreen(
                    onBackClick = { navController.popBackStack() },
                    onSaveClick = { navController.popBackStack() }
                )
            }
            composable("edit_preferences") {
                EditPreferencesScreen(
                    onBackClick = { navController.popBackStack() },
                    onSaveClick = { navController.popBackStack() }
                )
            }
            composable("change_password") {
                ChangePasswordScreen(
                    onBackClick = { navController.popBackStack() },
                    onSaveClick = { navController.popBackStack() }
                )
            }
            composable(
                "local_detail/{lugarId}",
                arguments = listOf(navArgument("lugarId") { type = NavType.IntType })
            ) { backStackEntry ->
                val lugarId = backStackEntry.arguments?.getInt("lugarId") ?: 0
                // Necesitamos obtener el lugar de alguna manera
                // Por simplicidad, creamos un lugar temporal o lo obtenemos del ViewModel
                val lugaresViewModel: LugaresViewModel = viewModel(
                    factory = LugaresViewModelFactory(LocalContext.current)
                )
                val lugaresState by lugaresViewModel.uiState.collectAsStateWithLifecycle()
                
                when (val state = lugaresState) {
                    is LugaresUiState.Success -> {
                        val lugar = state.lugares.find { it.id == lugarId }
                        lugar?.let {
                            LocalDetailScreen(
                                navController = navController,
                                lugar = it
                            )
                        } ?: run {
                            // Si no se encuentra el lugar, navegar de vuelta
                            LaunchedEffect(Unit) {
                                navController.popBackStack()
                            }
                        }
                    }
                    else -> {
                        // Mostrar loading o error mientras se cargan los lugares
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = GreenPrimary)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ForgotPasswordScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var error by remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    fun validate(): Boolean {
        return email.isNotBlank()
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight),
        contentAlignment = Alignment.Center
    ) {
        val maxWidth = maxWidth
        val horizontalPadding = if (maxWidth < 400.dp) 16.dp else 32.dp
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = horizontalPadding)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                text = "Recuperar Contraseña",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = GreenPrimary,
                    fontSize = 32.sp
                ),
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Text(
                text = "Te enviaremos un correo para restablecer tu contraseña",
                color = LightGrayText,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Correo electrónico") },
                singleLine = true,
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.fillMaxWidth(),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = GreenPrimary,
                    unfocusedBorderColor = LightGrayText
                )
            )
            if (error != null) {
                Text(error!!, color = MaterialTheme.colorScheme.error)
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = {
                if (!validate()) {
                    error = "Introduce tu correo."
                } else {
                    Toast.makeText(context, "¡Correo enviado! (simulado)", Toast.LENGTH_SHORT).show()
                    navController.popBackStack()
                }
            },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
            ) {
                Text("Enviar", color = Color.White, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text("Volver", color = GreenPrimary, fontSize = 18.sp)
            }
            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun SocialIconButton(iconRes: Int, contentDescription: String, onClick: () -> Unit) {
    Surface(
        shape = CircleShape,
        color = Color.White,
        shadowElevation = 4.dp,
        modifier = Modifier
            .size(48.dp)
            .clickable { onClick() }
    ) {
        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = contentDescription,
                tint = Color.Unspecified,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
fun MainAppScreen(navController: NavHostController) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.padding(32.dp)
        ) {
            Text(
                text = "Vida Sana UCSC",
                style = MaterialTheme.typography.headlineMedium.copy(
                    color = GreenPrimary,
                    fontSize = 32.sp
                ),
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Text(
                text = "¡Bienvenido a tu app de vida saludable!",
                color = LightGrayText,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(bottom = 32.dp)
            )
            Button(
                onClick = {
                    navController.navigate("login") {
                        popUpTo("main") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(24.dp),
                colors = ButtonDefaults.buttonColors(containerColor = GreenPrimary)
            ) {
                Text("Cerrar Sesión", color = Color.White, fontSize = 18.sp)
            }
        }
    }
}