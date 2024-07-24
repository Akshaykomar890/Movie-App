package com.example.movieapp.moviesList.util



sealed class GetResult<T>(
    val data:T? = null,
    val message:String? = null
) {

    class Success<T>(data: T?) : GetResult<T>(data)

    class Failure<T>(message: String,data: T? = null):GetResult<T>(data, message)


    class Loading<T>(isLoading:Boolean):GetResult<T>(null)

}