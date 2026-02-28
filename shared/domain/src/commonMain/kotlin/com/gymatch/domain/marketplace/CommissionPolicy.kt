package com.gymatch.domain.marketplace

class CommissionPolicy(
    private val baseCommissionBps: Int = 1200,
) {
    fun calculate(grossAmount: Long): Pair<Long, Long> {
        val commission = (grossAmount * baseCommissionBps) / 10_000
        val coachNet = grossAmount - commission
        return commission to coachNet
    }
}
