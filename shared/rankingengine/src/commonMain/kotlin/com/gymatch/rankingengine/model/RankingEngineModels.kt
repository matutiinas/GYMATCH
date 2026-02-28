package com.gymatch.rankingengine.model

data class RankingSignal(
    val baseScore: Double,
    val verifiedWorkoutCount: Int,
    val premiumBoostEligible: Boolean,
)

data class RankingOutcome(
    val finalScore: Double,
    val visibilityBoost: Double,
)
