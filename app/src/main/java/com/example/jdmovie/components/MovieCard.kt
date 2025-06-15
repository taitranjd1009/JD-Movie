package com.example.jdmovie.components

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.jdmovie.models.Film
import com.example.jdmovie.navigation.Screens
import com.google.gson.Gson
import java.net.URLEncoder

@Composable
fun MovieCard(navController: NavController, film: Film) {
    Card(

        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(onClick = {
                val filmJson = Gson().toJson(film)
                val filmEncoded = URLEncoder.encode(filmJson, "UTF-8")
                navController.navigate(Screens.MovieDetailRoute.route+"/${filmEncoded}")
            }),
//        colors = CardDefaults.cardColors()
    ) {

        AsyncImage(
            model = film.poster_url,
            contentDescription = null,
            modifier = Modifier
                .height(150.dp)
                .fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "Phim: ${film.name}",
                maxLines = 2,
                minLines = 1,
                style = MaterialTheme.typography.bodyLarge,
                color = Color.Blue,
                overflow = TextOverflow.Ellipsis,
            )

        }
    }
}
