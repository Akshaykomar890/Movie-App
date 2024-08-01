package com.example.movieapp.di

import com.example.movieapp.moviesList.data.repository.MovieRepositoryImp
import com.example.movieapp.moviesList.domain.repository.MovieListRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

  @Module
  @InstallIn(SingletonComponent::class)
  abstract class RepositoryModule {

      @Binds
      @Singleton
      abstract fun bindMovieListRepository(
          movieListRepositoryImpl: MovieRepositoryImp
      ): MovieListRepository

  }