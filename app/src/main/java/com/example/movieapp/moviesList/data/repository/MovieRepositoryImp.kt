package com.example.movieapp.moviesList.data.repository

import coil.network.HttpException
import com.example.movieapp.moviesList.data.local.movie.MovieDatabase
import com.example.movieapp.moviesList.data.mapper.toMovie
import com.example.movieapp.moviesList.data.mapper.toMovieEntity
import com.example.movieapp.moviesList.data.remote.MovieApi
import com.example.movieapp.moviesList.domain.model.Movie
import com.example.movieapp.moviesList.domain.repository.MovieListRepository
import com.example.movieapp.moviesList.util.GetResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.IOException
import javax.inject.Inject



class MovieRepositoryImp @Inject constructor(
    private val movieApi: MovieApi,
    private val database: MovieDatabase
): MovieListRepository {
    override suspend fun getMovieList(
        forceFetchFromRemote: Boolean,
        category: String,
        page: Int
    ): Flow<GetResult<List<Movie>>> {
        return flow {
                emit(GetResult.Loading(true))
            //first we get it from room if not from api
            //get list of movie from database
            var localMovieList = database.movieDao.getMovieByCategory(category)
            //if database is not empty then push to result
            val shouldLoadLocal = localMovieList.isNotEmpty()  &&  !forceFetchFromRemote
   
            if (shouldLoadLocal){
                emit(GetResult.Success(
                    //we need return as list of movie but we have movie entity so do mappers
                    data =  localMovieList.map {
                        movieEntity ->
                        movieEntity.toMovie(category)
                    }
                ))
                emit(GetResult.Loading(false))
                return@flow
            }



            //IF data base is empty get it from api
            val getMovieListFromApi = try{
                movieApi.getMoviesList(category, page)
            }catch (e: IOException){
                e.printStackTrace()
                emit(GetResult.Failure("Error Loading Data"))
                return@flow
            }catch (e:HttpException){
                e.printStackTrace()
                emit(GetResult.Failure("Error Loading Data"))
                return@flow
            }catch (e: Exception) {
                e.printStackTrace()
                emit(GetResult.Failure(message = "Error loading movies"))
                return@flow
            }

            val movieEntityList = getMovieListFromApi.results.map {
                movieDto ->
                movieDto.toMovieEntity(category)
            }
            //Store in database
            database.movieDao.upsertMovieList(movieEntityList)
            //pass data to result after storing in database
            emit(GetResult.Success(
                movieEntityList.map {
                    it->
                    it.toMovie(category)
                }
            ))
            emit(GetResult.Loading(false))
            return@flow
        }
    }

    override suspend fun getMovie(id: Int): Flow<GetResult<Movie>> {
        return flow {
            emit(GetResult.Loading(true))

            val movieEntity = database.movieDao.getMovieById(id)

            if (movieEntity != null){
                emit(GetResult.Success(
                    movieEntity.toMovie(movieEntity.category)
                ))
                emit(GetResult.Loading(false))
                return@flow
            }

            emit(GetResult.Failure("Error no such movie"))

            emit(GetResult.Loading(false))
        }

    }

}