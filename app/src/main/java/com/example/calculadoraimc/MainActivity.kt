package com.example.calculadoraimc

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.calculadoraimc.datasource.database.AppDatabase
import com.example.calculadoraimc.datasource.database.IMCDao
import com.example.calculadoraimc.feature.details.view.DetailsScreen
import com.example.calculadoraimc.feature.history.view.HistoryScreen
import com.example.calculadoraimc.feature.home.view.Home
import com.example.calculadoraimc.ui.theme.CalculadoraIMCTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 1. Inicializa o Banco de Dados
        val db = AppDatabase.getDatabase(applicationContext)
        val dao = db.imcDao()

        setContent {
            CalculadoraIMCTheme {
                MainAppNavigation(dao)
            }
        }
    }
}

object AppRoutes {
    const val HOME = "home"
    const val HISTORY = "history"
    const val DETAILS = "details/{imcId}"
}

@Composable
fun MainAppNavigation(imcDao: IMCDao) {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = AppRoutes.HOME) {

        // --- 1. TELA HOME (Corrigido: Só aparece uma vez agora) ---
        composable(AppRoutes.HOME) {
            Home(
                imcDao = imcDao,
                onNavigateToHistory = {
                    navController.navigate(AppRoutes.HISTORY)
                }
            )
        }

        // --- 2. TELA DE HISTÓRICO ---
        composable(AppRoutes.HISTORY) {
            HistoryScreen(
                imcDao = imcDao,
                onItemClick = { imcId ->
                    navController.navigate("details/$imcId")
                },
                onBack = { navController.popBackStack() }
            )
        }

        // --- 3. TELA DE DETALHES ---
        composable(
            route = AppRoutes.DETAILS,
            arguments = listOf(navArgument("imcId") { type = NavType.IntType })
        ) { backStackEntry ->
            val imcId = backStackEntry.arguments?.getInt("imcId") ?: 0

            DetailsScreen(
                imcId = imcId,
                imcDao = imcDao,
                onBack = { navController.popBackStack() }
            )
        }
    }
}