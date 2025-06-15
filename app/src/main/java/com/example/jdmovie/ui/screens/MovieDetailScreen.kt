package com.example.jdmovie.ui.screens

import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil3.compose.SubcomposeAsyncImage
import com.example.jdmovie.MoviePlayerActivity
import com.example.jdmovie.R
import com.example.jdmovie.components.MovieDetailTabs
import com.example.jdmovie.domain.util.asHtmlText
import com.example.jdmovie.models.Episode
import com.example.jdmovie.models.Film
import com.example.jdmovie.viewmodels.MovieDetailScreenViewModel


sealed interface MovieDetailViewState {
    object Loading : MovieDetailViewState
    data class Error(val message: String) : MovieDetailViewState
    data class Success(
        val film: Film,
        val episodes: List<Episode>
    ) : MovieDetailViewState
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    navController: NavController,
    movie: Film,
    viewModel: MovieDetailScreenViewModel = hiltViewModel()
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val context = LocalContext.current
    val state by viewModel.stateFlow.collectAsState()

    val successState = state as? MovieDetailViewState.Success
    val isLoading = state is MovieDetailViewState.Loading
    val errorMessage = (state as? MovieDetailViewState.Error)?.message

    LaunchedEffect(key1 = Unit, block = {
        viewModel.fetchMovieDetail(movie.slug)
    })

    Scaffold() { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
            ) {

                SubcomposeAsyncImage(
                    model = if (movie.thumb_url.startsWith("http")) movie.thumb_url else "https://phimimg.com/${movie.thumb_url}",
                    contentDescription = null,
                    loading = {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    },
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier.matchParentSize()
                )

                IconButton(
                    onClick = { navController.popBackStack() },
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.2f),
                            shape = CircleShape
                        )
                        .size(42.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.White
                    )
                }
            }

            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = movie.origin_name.asHtmlText(),
                    style = MaterialTheme.typography.titleLarge
                )
                Text(text = movie.name)
                Row(

                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceEvenly,
                        modifier = Modifier.border(
                            1.dp,
                            Color.Black,
                            shape = RoundedCornerShape(100.dp)
                        )
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_schedule_24),
                            contentDescription = "back"
                        )
                        Text(text = movie.time, style = MaterialTheme.typography.labelSmall)
                    }
//                    Text(text = "sdcsdcds")
                }
                when {
                    isLoading -> {
                        Text(text = "Đang tải nội dung...")
                    }

                    errorMessage != null -> {
                        Text(text = errorMessage, color = Color.Red)
                    }

                    successState != null -> {
                        Text(
                            text = successState.film.content,
                            maxLines = 5,
                            overflow = TextOverflow.Ellipsis
                        )
//                        Button(onClick = {
//                            val intent = Intent(context, MoviePlayerActivity::class.java)
//                            intent.putExtra(
//                                "movieKey",
//                                successState.episodes[0].server_data.
//                            )
//                            context.startActivity(intent)
//                        }) {
//                            Text(text = "Xem phim")
//                        }
                    }
                }


            }
            when {
                isLoading -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
                }

                errorMessage != null -> {
                    Text(text = "Không tải được tập: $errorMessage", color = Color.Red)
                }

                successState != null -> {
                    MovieDetailTabs(
                        modifier = Modifier,
                        contentPadding = PaddingValues(),
                        episodes = successState.episodes
                    )
                }
            }

        }
    }
}


