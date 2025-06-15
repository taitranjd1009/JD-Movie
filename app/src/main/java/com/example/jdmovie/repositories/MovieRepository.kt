package com.example.jdmovie.repositories

import com.example.jdmovie.data.remote.MovieApi
import com.example.jdmovie.domain.util.ApiOperation
import com.example.jdmovie.models.Film
import com.example.jdmovie.models.FilmResponse
import retrofit2.Response
import javax.inject.Inject

class MovieRepository @Inject constructor(private val movieApi: MovieApi) {
    suspend fun fetchMovies(): ApiOperation<FilmResponse> {
        return try {
            val response = movieApi.getMovies()
            ApiOperation.Success(response)
        } catch (e: Exception) {
            ApiOperation.Failure(e)
        }
    }

    suspend fun searchMovies(keyword: String): ApiOperation<FilmResponse> {
        return try {
            val response = movieApi.getSearch(keyword)
            ApiOperation.Success(response)
        } catch (e: Exception) {
            ApiOperation.Failure(e)
        }
    }

    suspend fun fetchMovieDetail(slug: String): ApiOperation<FilmResponse> {
        return try {
            val response = movieApi.getMovieDetail(slug)
            ApiOperation.Success(response)
        } catch (e: Exception) {
            ApiOperation.Failure(e)
        }
    }
}