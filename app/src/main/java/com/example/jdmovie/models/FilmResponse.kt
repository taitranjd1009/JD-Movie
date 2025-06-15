package com.example.jdmovie.models

data class FilmResponse(
    val status: Boolean,
    val items: List<Film>,
    val pagination: Pagination,
    val movie: Film,
    val msg: String,
    val episodes: List<Episode>,
    val data: Data,
)

data class Data(
    val items: List<Film>,
)

data class Pagination(
    val totalItems: Int,
    val totalItemsPerPage: Int,
    val currentPage: Int,
    val totalPages: Int
)

data class Film(
    val _id: String,
    val name: String,
    val origin_name: String,
    val slug: String,
    val year: Int,
    val poster_url: String,
    val thumb_url: String,
    val tmdb: Tmdb,
    val imdb: Imdb,
    val modified: Modified,
    val time: String,
    val content: String,
    val episode_total: String
)

data class Tmdb(
    val type: String?,
    val id: String?,
    val season: Int?,
    val vote_average: Double,
    val vote_count: Int
)

data class Imdb(val id: String?)
data class Modified(val time: String)

data class Episode(
    val server_name: String,
    val server_data: List<EpisodeItem>
)

data class EpisodeItem(
    val name: String,
    val slug: String,
    val filename: String,
    val link_m3u8: String
)
