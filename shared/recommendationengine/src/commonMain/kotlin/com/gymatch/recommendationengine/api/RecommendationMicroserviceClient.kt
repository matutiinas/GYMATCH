package com.gymatch.recommendationengine.api

import com.gymatch.domain.recommendation.CandidateRecommendation
import com.gymatch.domain.recommendation.RecommendationRequest

interface RecommendationMicroserviceClient {
    suspend fun rank(request: RecommendationRequest): List<CandidateRecommendation>
}
