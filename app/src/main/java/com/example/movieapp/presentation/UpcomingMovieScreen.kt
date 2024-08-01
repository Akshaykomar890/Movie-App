package com.example.movieapp.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.movieapp.moviesList.presentation.MovieListState
import com.example.movieapp.moviesList.presentation.MovieListUIEvent
import com.example.movieapp.moviesList.util.Category
import com.example.movieapp.presentation.component.MovieItem


@Composable
fun UpcomingMovieScreen(
    movieState:MovieListState,
    navHostController: NavHostController,
    onEvent:(MovieListUIEvent)->Unit

){
    if (movieState.upcomingMovieList.isEmpty()){
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    }
    else{

        LazyVerticalGrid(
            modifier = Modifier
                .fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp)
            ,
            columns = GridCells.Fixed(2)
        ) {
            items(movieState.upcomingMovieList.size){
                index->
                MovieItem(movie = movieState.upcomingMovieList[index], navHostController = navHostController)

                Spacer(modifier = Modifier.height(16.dp))

                if (index >= movieState.upcomingMovieList.size-1){
                    onEvent(MovieListUIEvent.Paginate(Category.UPCOMING))
                }

            }


            }

        }

    }

