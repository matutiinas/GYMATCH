package com.gymatch.shared
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class DiscoverViewModel(
  private val getDiscoverProfiles: GetDiscoverProfilesUseCase
) {
  private val _state = MutableStateFlow<DiscoverUiState>(DiscoverUiState.Loading)
  val state: StateFlow<DiscoverUiState> = _state.asStateFlow()

  init {
    runCatching { getDiscoverProfiles() }
      .onSuccess { profiles ->
        _state.value = if (profiles.isEmpty()) DiscoverUiState.Empty else DiscoverUiState.Success(profiles)
      }
      .onFailure { e ->
        _state.value = DiscoverUiState.Error(e.message ?: "Error desconocido")
      }
  }
}
