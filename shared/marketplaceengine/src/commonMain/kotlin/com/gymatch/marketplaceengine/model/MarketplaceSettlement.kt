package com.gymatch.marketplaceengine.model

data class SettlementInput(
    val grossAmount: Long,
    val commissionBps: Int,
)

data class SettlementResult(
    val commissionAmount: Long,
    val coachNetAmount: Long,
)
