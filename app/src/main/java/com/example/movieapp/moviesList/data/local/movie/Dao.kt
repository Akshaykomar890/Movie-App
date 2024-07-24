package com.example.movieapp.moviesList.data.local.movie

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert



@Dao
interface Dao {
    @Upsert
    suspend fun upsertMovieList(movieList:List<MovieEntity>)

    @Query("SELECT * FROM MovieEntity WHERE id = :id")
    suspend fun getMovieById(id:Int):MovieEntity

    @Query("SELECT * FROM MovieEntity WHERE id = :category")
    suspend fun getMovieByCategory(category:String):List<MovieEntity>

}