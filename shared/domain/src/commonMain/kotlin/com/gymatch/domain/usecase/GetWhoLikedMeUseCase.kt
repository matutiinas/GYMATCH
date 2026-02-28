package com.gymatch.domain.usecase

import com.gymatch.domain.policy.RestrictionPolicyService
import com.gymatch.domain.repository.DiscoverRepository

class GetWhoLikedMeUseCase(
    private val discoverRepository: DiscoverRepository,
    private val restrictionPolicyService: RestrictionPolicyService,
) {
    suspend operator fun invoke(userId: String): List<String> {
        val restrictions = restrictionPolicyService.getUserRestrictions(userId)
        require(restrictions.canSeeWhoLikedYou) { "Premium required to view likes." }
        return discoverRepository.getWhoLikedMe(userId)
    }
}
