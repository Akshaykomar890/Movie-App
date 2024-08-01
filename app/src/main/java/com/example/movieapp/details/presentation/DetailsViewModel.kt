package com.example.movieapp.details.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.moviesList.domain.repository.MovieListRepository
import com.example.movieapp.moviesList.util.GetResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val movieRepository:MovieListRepository,
    private val savedStateHandle: SavedStateHandle //getting arguments
) :ViewModel(){

    private val movieId = savedStateHandle.get<Int>("movieId")

    private var _detailsState = MutableStateFlow(DetailsState())
    val detailsState = _detailsState.asStateFlow()


    init {
        getMovieById(movieId?:-1)
    }

    private fun getMovieById(movieId:Int){
        viewModelScope.launch {
            _detailsState.update {
                it->
                it.copy(isLoading = true)
            }
            movieRepository.getMovie(movieId).collectLatest {
                result->
                when(result){
                    is GetResult.Failure -> {
                        _detailsState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is GetResult.Loading -> {
                        _detailsState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                    is GetResult.Success ->{
                        result.data?.let {
                            movie->
                            _detailsState.update {
                                it.copy(movie = movie)
                            }
                        }

                    }
                }
            }
        }

    }





}