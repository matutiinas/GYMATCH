package com.gymatch.recommendationengine.model

data class RecommendationScoringConfig(
    val collaborativeWeight: Double,
    val compatibilityWeight: Double,
    val eloWeight: Double,
)
