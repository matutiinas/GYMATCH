package com.gymatch.shared

sealed interface DiscoverUiState {
  data object Loading : DiscoverUiState
  data object Empty : DiscoverUiState
  data class Error(val message: String) : DiscoverUiState
  data class Success(val profiles: List<Profile>) : DiscoverUiState
}
