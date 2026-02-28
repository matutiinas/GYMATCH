package com.gymatch.domain.recommendation

data class RecommendationRequest(
    val userId: String,
    val candidateIds: List<String>,
    val collaborativeWeight: Double = 0.45,
    val compatibilityWeight: Double = 0.35,
    val eloWeight: Double = 0.20,
)

data class CandidateRecommendation(
    val candidateId: String,
    val compatibilityScore: Double,
    val collaborativeScore: Double,
    val predictedMatchProbability: Double,
    val finalScore: Double,
)

interface RecommendationService {
    suspend fun rankCandidates(request: RecommendationRequest): List<CandidateRecommendation>
}

interface RecommendationFeatureProvider {
    suspend fun getUserVector(userId: String): RecommendationVector
    suspend fun getCandidateVector(candidateId: String): RecommendationVector
    suspend fun getCollaborativeAffinity(userId: String, candidateId: String): Double
}

data class RecommendationVector(
    val sports: Set<String>,
    val objectives: Set<String>,
    val level: Int,
    val activityRecencyDays: Int,
    val sportsElo: Double,
)
