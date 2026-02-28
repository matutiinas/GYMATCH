package com.gymatch.domain.ranking

import kotlin.math.max

data class SportEloInput(
    val currentElo: Double,
    val weeklyFrequency: Int,
    val performanceScore: Double,
    val regularityRatio: Double,
)

class SportEloCalculator(
    private val kFactor: Double = 24.0,
) {
    fun calculate(input: SportEloInput): Double {
        val expected = 1.0 / (1.0 + Math.pow(10.0, (1000.0 - input.currentElo) / 400.0))
        val actual = (
            (input.weeklyFrequency.toDouble() / max(1, 7)).coerceIn(0.0, 1.0) * 0.35 +
                input.performanceScore.coerceIn(0.0, 1.0) * 0.40 +
                input.regularityRatio.coerceIn(0.0, 1.0) * 0.25
            ).coerceIn(0.0, 1.0)

        return (input.currentElo + kFactor * (actual - expected)).coerceIn(600.0, 2400.0)
    }
}
