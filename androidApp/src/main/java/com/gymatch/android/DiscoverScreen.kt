package com.gymatch.android

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.gymatch.shared.DiscoverUiState
import com.gymatch.shared.DiscoverViewModel
import com.gymatch.shared.Profile

private enum class DiscoverSection(val label: String) {
  Discover("Descubrir"),
  Matches("Matches"),
  Chat("Chat")
}

@Composable
fun DiscoverScreen(viewModel: DiscoverViewModel) {
  val uiState by viewModel.state.collectAsState()
  var selectedSection by rememberSaveable { mutableStateOf(DiscoverSection.Discover) }

  when (val state = uiState) {
    DiscoverUiState.Loading -> Text("Cargando…", modifier = Modifier.padding(16.dp))
    DiscoverUiState.Empty -> Text("No hay perfiles", modifier = Modifier.padding(16.dp))
    is DiscoverUiState.Error -> Text(
      text = state.message,
      modifier = Modifier.padding(16.dp),
      color = MaterialTheme.colorScheme.error
    )
    is DiscoverUiState.Success -> {
      Column(modifier = Modifier.fillMaxSize()) {
        TabRow(selectedTabIndex = selectedSection.ordinal) {
          DiscoverSection.entries.forEach { section ->
            Tab(
              selected = selectedSection == section,
              onClick = { selectedSection = section },
              text = { Text(section.label) }
            )
          }
        }

        when (selectedSection) {
          DiscoverSection.Discover -> DiscoverProfilesSection(profiles = state.profiles)
          DiscoverSection.Matches -> MatchesSection(profiles = state.profiles)
          DiscoverSection.Chat -> ChatSection(profiles = state.profiles)
        }
      }
    }
  }
}

@Composable
private fun DiscoverProfilesSection(profiles: List<Profile>) {
  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    items(profiles, key = { it.id }) { profile ->
      SwipeProfileCard(profile = profile)
    }
  }
}

@Composable
private fun SwipeProfileCard(profile: Profile) {
  var action by remember(profile.id) { mutableStateOf("Pendiente") }

  Card(modifier = Modifier.fillMaxWidth()) {
    Column(
      modifier = Modifier.padding(16.dp),
      verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
      Text(
        text = "${profile.name}, ${profile.age}",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.SemiBold
      )
      Text(text = "Deporte: ${profile.sport}")
      Text(text = "Nivel: ${profile.level}")
      Text(text = profile.bio)
      Text(text = "Estado swipe: $action", style = MaterialTheme.typography.labelMedium)

      Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        Button(onClick = { action = "Descartado" }) {
          Text("Pasar")
        }
        Button(onClick = { action = "Like" }) {
          Text("Like")
        }
      }
    }
  }
}

@Composable
private fun MatchesSection(profiles: List<Profile>) {
  val matches = profiles.take(4)

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    if (matches.isEmpty()) {
      item { Text("Aún no tienes matches") }
    } else {
      items(matches, key = { it.id }) { profile ->
        Card(modifier = Modifier.fillMaxWidth()) {
          Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text("Match con ${profile.name}", style = MaterialTheme.typography.titleMedium)
            Text("${profile.sport} · ${profile.level}")
            Text("Sugerencia: inicia conversación para entrenar esta semana.")
          }
        }
      }
    }
  }
}

@Composable
private fun ChatSection(profiles: List<Profile>) {
  val chats = profiles.take(3)

  LazyColumn(
    modifier = Modifier.fillMaxSize(),
    contentPadding = PaddingValues(16.dp),
    verticalArrangement = Arrangement.spacedBy(12.dp)
  ) {
    if (chats.isEmpty()) {
      item { Text("No hay chats activos") }
    } else {
      items(chats, key = { it.id }) { profile ->
        Card(modifier = Modifier.fillMaxWidth()) {
          Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
            Text(profile.name, style = MaterialTheme.typography.titleMedium)
            Text("Último mensaje: ¿Entrenamos ${profile.sport.lowercase()} mañana?")
          }
        }
      }
    }
  }
}
