package com.example.movieapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.graphics.Color
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.compose.MovieAppTheme
import com.example.movieapp.details.presentation.DetailsScreen
import com.example.movieapp.moviesList.presentation.MovieListViewModel
import com.example.movieapp.moviesList.util.Screen
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieAppTheme { 
                SetBarColor(color = MaterialTheme.colorScheme.inverseSurface)
                val navController = rememberNavController()
                NavHost(navController = navController,
                    startDestination = Screen.Home.route
                ){
                    composable(Screen.Home.route){
                        HomeScreen(navController)
                    }
                    composable(
                        Screen.Details.route+"/{movieId}",
                        arguments = listOf(
                            navArgument("movieId"){ type = NavType.IntType}
                        )
                    ){ backStackEntry->
                        DetailsScreen(backStackEntry = backStackEntry)
                    }


                }

            }
        }
    }

    @Composable
    private fun SetBarColor(color:Color){
        val systemUiController = rememberSystemUiController()
        LaunchedEffect(key1 = color) {
            systemUiController.setSystemBarsColor(color)
        }
    }
}

