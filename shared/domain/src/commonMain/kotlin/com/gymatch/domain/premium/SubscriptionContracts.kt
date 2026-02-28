package com.gymatch.domain.premium

interface SubscriptionRepository {
    suspend fun getStatus(userId: String): SubscriptionStatus
    suspend fun startPremiumMonthly(userId: String): SubscriptionStatus
    suspend fun cancelAutoRenew(userId: String)
}

interface SubscriptionManager {
    suspend fun getRestrictionSnapshot(userId: String): RestrictionSnapshot
}

interface FeatureFlagProvider {
    fun isEnabled(flag: PremiumFeatureFlag): Boolean
}

enum class PremiumFeatureFlag {
    PREMIUM_ENABLED,
    PAYWALL_FOR_LIKE_LIST,
    BOOST_ENABLED,
    EXTENDED_MATCH_LIMIT_ENABLED,
    ADS_IN_FREE_TIER,
}
