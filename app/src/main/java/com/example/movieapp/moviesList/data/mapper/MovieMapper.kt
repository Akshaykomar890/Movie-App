package com.example.movieapp.moviesList.data.mapper

import com.example.movieapp.moviesList.data.local.movie.MovieEntity
import com.example.movieapp.moviesList.data.remote.respond.MovieDto
import com.example.movieapp.moviesList.domain.model.Movie



fun MovieDto.toMovieEntity(
    category: String
):MovieEntity{

    return MovieEntity(
        adult = adult ?: false,
        backdrop_path = backdrop_path ?: "",
        original_language = original_language ?: "",
        overview = overview ?: "",
        poster_path = poster_path ?: "",
        release_date = release_date ?: "",
        title = title ?: "",
        vote_average = vote_average ?: 0.0,
        popularity = popularity ?: 0.0,
        vote_count = vote_count ?: 0,
        id = id ?: -1,
        original_title = original_title ?: "",
        video = video ?: false,

        category = category,
        genre_ids = try {
            genre_ids?.joinToString(",")?:"-1,-2" //no data -1,-2 default value
        } catch (e: Exception) {
            "-1,-2"
        }
    )
}

fun MovieEntity.toMovie(
    category:String
):Movie{
    return Movie(
        adult = adult,
        backdrop_path = backdrop_path,
        original_language = original_language,
        original_title = original_title,
        overview = overview,
        popularity = popularity,
        poster_path=poster_path,
        release_date =release_date,
        title=title,
        video = video,
        vote_average = vote_average,
        vote_count = vote_count,
        id = id,

        category = category,
        //we cant store list of ids in database so string converted from movie DTO, but to get as Movie we need to convert
        genre_ids = try {
            //convert back to string to list of integer
            genre_ids.split(",")
                .map {
                    it.toInt()
                }
        }catch (e:Exception){
            listOf(-1,-2)
        }
    )
}