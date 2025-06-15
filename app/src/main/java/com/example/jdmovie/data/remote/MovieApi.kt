package com.example.jdmovie.data.remote

import com.example.jdmovie.domain.util.ApiOperation
import com.example.jdmovie.models.Film
import com.example.jdmovie.models.FilmResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface MovieApi {
    @GET("/danh-sach/phim-moi-cap-nhat-v3?page=0")
    suspend fun getMovies(): FilmResponse

    @GET("/v1/api/tim-kiem")
    suspend fun getSearch(@Query("keyword") keyword: String): FilmResponse

    @GET("/phim/{slug}")
    suspend fun getMovieDetail(@Path("slug") slug: String): FilmResponse
}