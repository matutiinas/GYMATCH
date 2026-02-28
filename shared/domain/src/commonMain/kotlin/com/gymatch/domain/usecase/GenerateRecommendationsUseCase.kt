package com.gymatch.domain.usecase

import com.gymatch.domain.recommendation.CandidateRecommendation
import com.gymatch.domain.recommendation.RecommendationRequest
import com.gymatch.domain.recommendation.RecommendationService

class GenerateRecommendationsUseCase(
    private val recommendationService: RecommendationService,
) {
    suspend operator fun invoke(userId: String, candidateIds: List<String>): List<CandidateRecommendation> {
        return recommendationService.rankCandidates(
            RecommendationRequest(
                userId = userId,
                candidateIds = candidateIds,
            )
        )
    }
}
