package com.example.jdmovie.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import com.example.jdmovie.models.Episode
import com.example.jdmovie.navigation.MovieDetailNavGraph
import com.example.jdmovie.viewmodels.MovieDetailScreenViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailTabs(modifier: Modifier, contentPadding: PaddingValues, episodes: List<Episode>, viewModel: MovieDetailScreenViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val startDestination = "0"
    var selectedDestination by rememberSaveable { mutableIntStateOf(0) }
    Column {
        ScrollableTabRow(
            selectedTabIndex = selectedDestination, edgePadding = 0.dp,
            modifier = Modifier.padding(contentPadding),
            indicator = { tabPositions ->
                val tab = tabPositions[selectedDestination]
                Box(
                    Modifier
                        .tabIndicatorOffset(tab)
                        .padding(horizontal = 16.dp) // Giống Material3
                        .height(3.dp)
                        .fillMaxWidth() // kết hợp với padding sẽ tạo hiệu ứng ngắn
                        .background(
                            color = MaterialTheme.colorScheme.primary,
                            shape = RoundedCornerShape(3.dp)
                        )
                )
            },
            divider = {}
        ) {
            episodes.forEachIndexed { index, item ->
                Tab(
                    selected = selectedDestination == index,
                    onClick = {
                        navController.navigate(route = index.toString())
                        selectedDestination = index
                    },
                    text = {
                        Text(
                            text = item.server_name,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                )
            }
        }
        MovieDetailNavGraph(navController, startDestination, episodes, viewModel)
    }
}