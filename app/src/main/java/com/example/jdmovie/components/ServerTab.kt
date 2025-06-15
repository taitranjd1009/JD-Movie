package com.example.jdmovie.components

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.AsyncImage
import com.example.jdmovie.MoviePlayerActivity
import com.example.jdmovie.models.Episode
import com.example.jdmovie.ui.screens.MovieDetailViewState
import com.example.jdmovie.viewmodels.MovieDetailScreenViewModel

fun navigateToPlayer(context: Context, videoUrl: String): () -> Unit {
    return {
        val intent = Intent(context, MoviePlayerActivity::class.java)
        intent.putExtra("movieKey", videoUrl)
        context.startActivity(intent)
    }

}

@Composable
fun ServerTab(
    navController: NavController,
    episode: Episode,
    viewModel: MovieDetailScreenViewModel = hiltViewModel()
) {
    val state by viewModel.stateFlow.collectAsState()
    val context = LocalContext.current
    when (val viewState = state) {
        MovieDetailViewState.Loading -> Text(text = "Loading")
        is MovieDetailViewState.Error -> Text(text = viewState.message)
        is MovieDetailViewState.Success -> {
            val data = viewState.film
            val totalEpisodes = data.episode_total?.toIntOrNull()
            if (totalEpisodes != null && totalEpisodes < 21) {
                LazyColumn() {
                    items(items = episode.server_data, key = { it.slug }) { item ->
                        Row(
                            modifier = Modifier
                                .padding(8.dp)
                                .combinedClickable(
                                    onClick = navigateToPlayer(
                                        context,
                                        item.link_m3u8
                                    )
                                )
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(width = 120.dp, height = 80.dp)
                            ) {
                                AsyncImage(
                                    model = viewState.film.thumb_url,
                                    contentDescription = null,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .clip(RoundedCornerShape(4.dp)),
                                    contentScale = ContentScale.Crop
                                )

                                Icon(
                                    imageVector = Icons.Default.PlayArrow,
                                    contentDescription = "Play",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .align(Alignment.Center)
                                        .size(24.dp)
                                        .background(
                                            color = Color.Black.copy(alpha = 0.5f),
                                            shape = CircleShape
                                        )
                                        .padding(2.dp)
                                        .border(
                                            1.dp,
                                            Color.White, shape = RoundedCornerShape(100.dp)
                                        )

                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(text = item.name, style = MaterialTheme.typography.titleLarge)
                                Text(
                                    text = item.filename,
                                    style = MaterialTheme.typography.titleSmall
                                )
                            }
                        }


                    }
                }
            } else {
                LazyVerticalGrid(
                    contentPadding = PaddingValues(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    columns = GridCells.Fixed(4),
                    content = {
                        items(items = episode.server_data, key = { it.slug }) { item ->
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(40.dp)
                                    .background(
                                        color = Color.Gray.copy(alpha = 0.5f),
                                        shape = RoundedCornerShape(8.dp)
                                    ).combinedClickable(
                                        onClick = navigateToPlayer(
                                            context,
                                            item.link_m3u8
                                        )
                                    )
                            ) {
                                Text(
                                    text = item.name,
                                    style = MaterialTheme.typography.titleSmall,
                                    textAlign = TextAlign.Center
                                )
                            }
                        }
                    }

                )
            }
        }
    }

}
