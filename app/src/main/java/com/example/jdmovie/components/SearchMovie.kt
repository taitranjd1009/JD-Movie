package com.example.jdmovie.components

import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.clearText
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.SearchBarDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.jdmovie.viewmodels.SearchScreenViewModel
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil3.compose.SubcomposeAsyncImage
import com.example.jdmovie.models.Film
import com.example.jdmovie.navigation.Screens
import com.google.gson.Gson
import java.net.URLEncoder

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SearchMovie(
    navController: NavController,
    searchViewModel: SearchScreenViewModel = hiltViewModel()
) {
    var searchQuery by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(false) }
    var expanded by rememberSaveable { mutableStateOf(false) }
    val screenState by searchViewModel.uiState.collectAsStateWithLifecycle()

    DisposableEffect(key1 = Unit) {
        val job = searchViewModel.observeMovieSearch()
        onDispose { job.cancel() }
    }

    SearchBar(
//        modifier = Modifier
//            .align(Alignment.TopCenter)
//            .semantics { traversalIndex = 0f },
        inputField = {
            SearchBarDefaults.InputField(
                query = searchViewModel.searchTextFieldState.text.toString(),
                onQueryChange = { searchViewModel.searchTextFieldState.edit { replace(0, length, it) } },
                onSearch = {
//                    onSearch(textFieldState.text.toString())
                    expanded = false
                },
                expanded = expanded,
                onExpandedChange = { expanded = it },
                placeholder = { Text("Search") }
            )
        },
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
       when (val state = screenState) {
           SearchScreenViewModel.ScreenState.Empty -> {
               Text(
                   text = "Search for characters!",
                   color = Color.White,
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(32.dp),
                   textAlign = TextAlign.Center,
                   fontSize = 26.sp
               )
           }
           SearchScreenViewModel.ScreenState.Searching -> {}
           is SearchScreenViewModel.ScreenState.Error -> {
               Text(
                   text = state.message,
                   color = Color.White,
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(32.dp),
                   textAlign = TextAlign.Center,
                   fontSize = 26.sp
               )

               Button(
//                   colors = ButtonDefaults.buttonColors().copy(containerColor = RickAction),
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(horizontal = 84.dp),
                   onClick = { searchViewModel.searchTextFieldState.clearText() }
               ) {
                   Text(text = "Clear search", color = Color.Red)
               }

           }
           is SearchScreenViewModel.ScreenState.Content -> {
               LazyColumn {
                   items(items = state.results) { item ->
                       SearchItem(item, navController)
                   }
               }
           }

       }
    }
}

@Composable
fun SearchItem(item: Film, navController: NavController){
    Row(modifier = Modifier.padding(8.dp).combinedClickable(onClick = {
        val filmJson = Gson().toJson(item)
        val filmEncoded = URLEncoder.encode(filmJson, "UTF-8")
        navController.navigate(Screens.MovieDetailRoute.route+"/${filmEncoded}")
    })) {
        SubcomposeAsyncImage(
            model = "https://phimimg.com/${item.thumb_url}", contentDescription = null,
            loading = {
                Box(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            },
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .width(80.dp)
                .height(80.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = item.name,maxLines = 2,
            minLines = 1,
            style = MaterialTheme.typography.titleSmall,
            color = Color.Black,
            overflow = TextOverflow.Ellipsis,)
    }
}