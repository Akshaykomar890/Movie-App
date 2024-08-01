package com.example.movieapp.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Upcoming
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Upcoming
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.movieapp.moviesList.presentation.MovieListUIEvent
import com.example.movieapp.moviesList.presentation.MovieListViewModel
import com.example.movieapp.moviesList.util.Screen



data class bottomListItems(
    val title:String,
    val selectedIcon:ImageVector,
    val unSelectedIcon:ImageVector,
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navHostController: NavHostController) {
    val movieListViewModel = hiltViewModel<MovieListViewModel>()
    val movieListState = movieListViewModel.movieListState.collectAsState().value
    val bottomNavController = rememberNavController()

    Scaffold(
        bottomBar = {
                BottomNavigationBar(
                    bottomNavController = bottomNavController,
                    onEvent = movieListViewModel::onEvent
                )
        },
        topBar = {
            TopAppBar(title = {
                Text(text = if(movieListState.isCurrentPopularScreen){
                    "Popular Movies"
                }
                else{
                    "Upcoming Movies"
                },
                    fontSize = 20.sp
                )
            },
                modifier = Modifier.shadow(2.dp),
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    MaterialTheme.colorScheme.inverseOnSurface
                )
            )
            
        }
    ){
        Box(modifier = Modifier
            .fillMaxSize()
            .padding(it)
        ){
          NavHost(navController = bottomNavController, startDestination = Screen.PopularMovieList.route ){
              composable(Screen.PopularMovieList.route){
                  PopularMovieScreen(movieListState,bottomNavController,movieListViewModel::onEvent)
              }
              composable(Screen.UpcomingMovieList.route){
                  //PopularScreen
              }
          }

        }

    }
}

@Composable
fun BottomNavigationBar(
    bottomNavController: NavHostController,
    onEvent:(MovieListUIEvent)->Unit
){
        val items = listOf(
            bottomListItems(
                title = "Popular",
                selectedIcon = Icons.Filled.Movie,
                unSelectedIcon = Icons.Outlined.Movie
            ),
            bottomListItems(
                title = "Upcoming",
                selectedIcon = Icons.Filled.Upcoming,
                unSelectedIcon = Icons.Outlined.Upcoming
            )

        )
    val selected = rememberSaveable {
        mutableIntStateOf(0)
    }

    NavigationBar {
        Row(
            modifier = Modifier.background(MaterialTheme.colorScheme.inverseOnSurface)
        ) {
            items.forEachIndexed { index, bottomListItems ->
                NavigationBarItem(
                    selected = selected.intValue == index,

                    onClick = {
                        selected.intValue = index
                        when(selected.intValue){
                            0->{
                                onEvent(MovieListUIEvent.Navigate)
                                bottomNavController.popBackStack()
                                bottomNavController.navigate(Screen.PopularMovieList.route)
                            }
                            1->{
                                onEvent(MovieListUIEvent.Navigate)
                                bottomNavController.popBackStack()
                                bottomNavController.navigate(Screen.UpcomingMovieList.route)
                            }
                        }
                    },


                    icon = {
                        Icon(
                            imageVector = if (selected.intValue == index){
                                bottomListItems.selectedIcon
                            } else {
                                bottomListItems.unSelectedIcon
                            },
                            contentDescription = null
                        )
                    },
                    label = {
                        Text(text = bottomListItems.title,
                            color = MaterialTheme.colorScheme.background)
                    }
                )
            }
        }

    }
}