package com.gymatch.domain.usecase

import com.gymatch.domain.model.MatchResult
import com.gymatch.domain.model.SwipeAction
import com.gymatch.domain.policy.RestrictionPolicyService
import com.gymatch.domain.repository.DiscoverRepository

class SubmitSwipeUseCase(
    private val discoverRepository: DiscoverRepository,
    private val restrictionPolicyService: RestrictionPolicyService,
) {
    suspend operator fun invoke(action: SwipeAction): MatchResult {
        val restrictions = restrictionPolicyService.getUserRestrictions(action.fromUserId)

        val swipeCount = discoverRepository.getDailySwipeCount(action.fromUserId)
        require(swipeCount < restrictions.maxDailySwipes) {
            "Daily swipe limit reached: ${restrictions.maxDailySwipes}"
        }

        if (!action.liked) return discoverRepository.submitSwipe(action)

        val matchCount = discoverRepository.getDailyMatchCount(action.fromUserId)
        require(matchCount < restrictions.maxDailyMatches) {
            "Daily match limit reached: ${restrictions.maxDailyMatches}"
        }

        return discoverRepository.submitSwipe(action)
    }
}
