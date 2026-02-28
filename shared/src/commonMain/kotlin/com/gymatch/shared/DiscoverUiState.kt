package com.gymatch.shared

sealed class DiscoverUiState {
    data object Loading : DiscoverUiState()
    data class Success(val profiles: List<Profile>) : DiscoverUiState()
    data object Empty : DiscoverUiState()
    data class Error(val message: String) : DiscoverUiState()
}
