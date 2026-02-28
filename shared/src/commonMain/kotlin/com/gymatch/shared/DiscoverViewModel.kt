package com.gymatch.shared

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class DiscoverViewModel(
    private val getDiscoverProfiles: GetDiscoverProfilesUseCase,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    private val _state = MutableStateFlow<DiscoverUiState>(DiscoverUiState.Loading)
    val state: StateFlow<DiscoverUiState> = _state.asStateFlow()

    init {
        loadProfiles()
    }

    fun loadProfiles() {
        scope.launch {
            _state.value = DiscoverUiState.Loading
            runCatching { getDiscoverProfiles() }
                .onSuccess { profiles ->
                    _state.value = if (profiles.isEmpty()) {
                        DiscoverUiState.Empty
                    } else {
                        DiscoverUiState.Success(profiles)
                    }
                }
                .onFailure { throwable ->
                    _state.value = DiscoverUiState.Error(throwable.message ?: "Unknown error")
                }
        }
    }
}
