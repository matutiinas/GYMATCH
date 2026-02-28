package com.gymatch.presentation.discover

import com.gymatch.domain.model.DiscoverCandidate
import com.gymatch.domain.model.SwipeAction
import com.gymatch.domain.usecase.SubmitSwipeUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class DiscoverUiState(
    val loading: Boolean = false,
    val currentCandidate: DiscoverCandidate? = null,
    val error: String? = null,
)

class DiscoverViewModel(
    private val submitSwipe: SubmitSwipeUseCase,
) {
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    private val _state = MutableStateFlow(DiscoverUiState())
    val state: StateFlow<DiscoverUiState> = _state.asStateFlow()

    fun onSwipeLeft(fromUserId: String, candidateId: String) {
        handleSwipe(fromUserId, candidateId, liked = false)
    }

    fun onSwipeRight(fromUserId: String, candidateId: String) {
        handleSwipe(fromUserId, candidateId, liked = true)
    }

    private fun handleSwipe(fromUserId: String, candidateId: String, liked: Boolean) {
        scope.launch {
            _state.value = _state.value.copy(loading = true, error = null)
            runCatching {
                submitSwipe(SwipeAction(fromUserId, candidateId, liked))
            }.onSuccess {
                _state.value = _state.value.copy(loading = false)
            }.onFailure { throwable ->
                _state.value = _state.value.copy(loading = false, error = throwable.message)
            }
        }
    }
}
