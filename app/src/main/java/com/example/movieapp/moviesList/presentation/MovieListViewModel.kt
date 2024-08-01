package com.example.movieapp.moviesList.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.movieapp.moviesList.domain.repository.MovieListRepository
import com.example.movieapp.moviesList.util.Category
import com.example.movieapp.moviesList.util.GetResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val movieListRepository: MovieListRepository
) :ViewModel(){

    private var _movieListState = MutableStateFlow(MovieListState())
    var movieListState = _movieListState.asStateFlow()


    init {
        getPopularMovieList(false)
        getUpcomingMovieList(false)
    }

    fun onEvent(event:MovieListUIEvent){
        when(event){
            MovieListUIEvent.Navigate -> {
                _movieListState.update {
                    it.copy(
                        isCurrentPopularScreen = !movieListState.value.isCurrentPopularScreen
                    )
                }

            }
            is MovieListUIEvent.Paginate -> {
                if (event.category == Category.POPULAR){
                    getPopularMovieList(true)
                }
                else if (event.category == Category.UPCOMING){
                    getUpcomingMovieList(true)
                }

            }
        }
    }


    private fun getPopularMovieList(forceFetchFromRemote: Boolean){
        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }
            movieListRepository.getMovieList(
                forceFetchFromRemote,
                category = Category.POPULAR,
                page = _movieListState.value.popularMovieListPage
            ).collectLatest {
                result->
                when(result){
                    is GetResult.Failure -> {
                        _movieListState.update {
                            it.copy(false)
                        }

                    }
                    is GetResult.Success -> {
                        result.data?.let {
                            popularList->
                            //exe a block of code with the object it is called on as the argument.
                            _movieListState.update {
                                it-> //it: Represents the current MovieListState object
                                it.copy(
                                    popularMovieList = _movieListState.value.popularMovieList
                                    +popularList.shuffled(),
                                    popularMovieListPage = _movieListState.value.popularMovieListPage
                                    +1
                                )
                            }


                        }
                    }
                    is GetResult.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                }


            }
        }


    }

    private fun getUpcomingMovieList(forceFetchFromRemote: Boolean){

        viewModelScope.launch {
            _movieListState.update {
                it.copy(isLoading = true)
            }

            movieListRepository.getMovieList(
                forceFetchFromRemote,
                Category.UPCOMING,
                page = _movieListState.value.upcomingMovieListPage
            ).collectLatest {
                result->
                when(result){

                    is GetResult.Failure -> {
                        _movieListState.update {
                            it.copy(isLoading = false)
                        }
                    }
                    is GetResult.Loading -> {
                        _movieListState.update {
                            it.copy(isLoading = result.isLoading)
                        }
                    }
                    is GetResult.Success -> {
                        result.data?.let {
                            upcomingList->
                            _movieListState.update {
                                it.copy(upcomingMovieList = _movieListState.value.upcomingMovieList
                                +upcomingList.shuffled(),
                                    upcomingMovieListPage = _movieListState.value.upcomingMovieListPage+1
                                )
                            }
                        }
                    }
                }
            }

        }
    }
}