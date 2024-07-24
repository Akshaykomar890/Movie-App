package com.example.movieapp.moviesList.domain.repository

import com.example.movieapp.moviesList.domain.model.Movie
import com.example.movieapp.moviesList.util.GetResult
import kotlinx.coroutines.flow.Flow


interface MovieListRepository {

    suspend fun getMovieList(
        forceFetchFromRemote:Boolean,
        category:String,
        page:Int
    ):Flow<GetResult<List<Movie>>>


    suspend fun getMovie(id:Int):Flow<GetResult<Movie>>


}