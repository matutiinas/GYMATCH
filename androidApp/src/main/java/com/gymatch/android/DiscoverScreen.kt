package com.gymatch.android

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.gymatch.shared.DiscoverUiState
import com.gymatch.shared.DiscoverViewModel
import com.gymatch.shared.Profile

@Composable
fun DiscoverScreen(viewModel: DiscoverViewModel) {
    val uiState by viewModel.state.collectAsState()

    when (val state = uiState) {
        DiscoverUiState.Loading -> {
            Text(
                text = "Cargando…",
                modifier = Modifier.padding(16.dp),
            )
        }

        DiscoverUiState.Empty -> {
            Text(
                text = "No hay perfiles",
                modifier = Modifier.padding(16.dp),
            )
        }

        is DiscoverUiState.Error -> {
            Text(
                text = state.message,
                modifier = Modifier.padding(16.dp),
                color = MaterialTheme.colorScheme.error,
            )
        }

        is DiscoverUiState.Success -> {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                items(state.profiles, key = { it.id }) { profile ->
                    ProfileCard(profile)
                }
            }
        }
    }
}

@Composable
private fun ProfileCard(profile: Profile) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(
                text = "${profile.name}, ${profile.age}",
                style = MaterialTheme.typography.titleMedium,
            )
            Text(text = "Deporte: ${profile.sport}")
            Text(text = "Nivel: ${profile.level}")
            Text(text = profile.bio)
        }
    }
}
