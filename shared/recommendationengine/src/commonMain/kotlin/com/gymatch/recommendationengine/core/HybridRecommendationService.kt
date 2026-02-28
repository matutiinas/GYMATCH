package com.gymatch.recommendationengine.core

import com.gymatch.domain.recommendation.CandidateRecommendation
import com.gymatch.domain.recommendation.RecommendationFeatureProvider
import com.gymatch.domain.recommendation.RecommendationRequest
import com.gymatch.domain.recommendation.RecommendationService
import com.gymatch.recommendationengine.api.RecommendationMicroserviceClient

class HybridRecommendationService(
    private val featureProvider: RecommendationFeatureProvider,
    private val remoteClient: RecommendationMicroserviceClient? = null,
) : RecommendationService {

    override suspend fun rankCandidates(request: RecommendationRequest): List<CandidateRecommendation> {
        remoteClient?.let { return it.rank(request) }

        val user = featureProvider.getUserVector(request.userId)
        return request.candidateIds.map { candidateId ->
            val candidate = featureProvider.getCandidateVector(candidateId)
            val collaborative = featureProvider.getCollaborativeAffinity(request.userId, candidateId).coerceIn(0.0, 1.0)
            val compatibility = compatibility(user, candidate)
            val eloInfluence = (candidate.sportsElo / 2400.0).coerceIn(0.0, 1.0)
            val final = (
                collaborative * request.collaborativeWeight +
                    compatibility * request.compatibilityWeight +
                    eloInfluence * request.eloWeight
                ).coerceIn(0.0, 1.0)

            CandidateRecommendation(
                candidateId = candidateId,
                compatibilityScore = compatibility,
                collaborativeScore = collaborative,
                predictedMatchProbability = logistic(final),
                finalScore = final,
            )
        }.sortedByDescending { it.finalScore }
    }

    private fun compatibility(
        user: com.gymatch.domain.recommendation.RecommendationVector,
        candidate: com.gymatch.domain.recommendation.RecommendationVector,
    ): Double {
        val sportsScore = jaccard(user.sports, candidate.sports)
        val objectiveScore = jaccard(user.objectives, candidate.objectives)
        val levelScore = 1.0 - (kotlin.math.abs(user.level - candidate.level).toDouble() / 5.0)
        val recencyScore = 1.0 - (kotlin.math.abs(user.activityRecencyDays - candidate.activityRecencyDays).toDouble() / 30.0)
        return (
            sportsScore * 0.35 + objectiveScore * 0.30 + levelScore.coerceIn(0.0, 1.0) * 0.20 +
                recencyScore.coerceIn(0.0, 1.0) * 0.15
            ).coerceIn(0.0, 1.0)
    }

    private fun jaccard(a: Set<String>, b: Set<String>): Double {
        if (a.isEmpty() && b.isEmpty()) return 1.0
        val union = a.union(b).size.toDouble()
        if (union == 0.0) return 0.0
        return a.intersect(b).size.toDouble() / union
    }

    private fun logistic(value: Double): Double = 1.0 / (1.0 + kotlin.math.exp(-8 * (value - 0.5)))
}
