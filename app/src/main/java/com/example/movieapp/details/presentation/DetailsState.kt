package com.example.movieapp.details.presentation

import com.example.movieapp.moviesList.domain.model.Movie

data class DetailsState(
    val isLoading:Boolean = false,
    val movie: Movie? = null
)