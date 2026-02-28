package com.gymatch.domain.matching

import kotlin.math.abs
import kotlin.math.max

data class MatchingSignal(
    val sports: Set<String>,
    val levelValue: Int,
    val objectives: Set<String>,
    val recentActivityDaysAgo: Int,
    val profileCompletion: Double,
)

data class MatchingWeights(
    val sportsWeight: Double = 0.35,
    val levelWeight: Double = 0.20,
    val objectiveWeight: Double = 0.20,
    val activityWeight: Double = 0.15,
    val profileWeight: Double = 0.10,
)

class MatchingScoreCalculator(
    private val weights: MatchingWeights = MatchingWeights(),
) {
    fun calculate(a: MatchingSignal, b: MatchingSignal): Double {
        val sportsScore = jaccard(a.sports, b.sports)
        val objectiveScore = jaccard(a.objectives, b.objectives)
        val levelScore = 1.0 - (abs(a.levelValue - b.levelValue).toDouble() / max(1, 5))
        val activityScore = 1.0 - (abs(a.recentActivityDaysAgo - b.recentActivityDaysAgo).toDouble() / 30.0)
        val completionScore = ((a.profileCompletion + b.profileCompletion) / 2.0)

        return normalize(
            (sportsScore * weights.sportsWeight) +
                (levelScore.coerceIn(0.0, 1.0) * weights.levelWeight) +
                (objectiveScore * weights.objectiveWeight) +
                (activityScore.coerceIn(0.0, 1.0) * weights.activityWeight) +
                (completionScore.coerceIn(0.0, 1.0) * weights.profileWeight)
        )
    }

    private fun jaccard(a: Set<String>, b: Set<String>): Double {
        if (a.isEmpty() && b.isEmpty()) return 1.0
        val intersection = a.intersect(b).size.toDouble()
        val union = a.union(b).size.toDouble()
        return if (union == 0.0) 0.0 else intersection / union
    }

    private fun normalize(value: Double): Double = value.coerceIn(0.0, 1.0)
}
