package com.example.movieapp.moviesList.data.local.movie

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [MovieEntity::class], version = 1,exportSchema = false)

abstract class MovieDatabase:RoomDatabase() {

    abstract val movieDao:Dao

}