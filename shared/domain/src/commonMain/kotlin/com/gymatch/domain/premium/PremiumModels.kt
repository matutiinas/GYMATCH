package com.gymatch.domain.premium

enum class SubscriptionTier { FREE, PREMIUM_MONTHLY }

enum class PremiumCapability {
    UNLIMITED_LIKES_VIEW,
    EXTENDED_MATCH_LIMIT,
    PROFILE_BOOST,
    SEE_WHO_LIKED_YOU,
}

data class SubscriptionStatus(
    val userId: String,
    val tier: SubscriptionTier,
    val isAutoRenewEnabled: Boolean,
    val validUntilEpochSec: Long?,
)

data class RestrictionSnapshot(
    val maxDailySwipes: Int,
    val maxDailyMatches: Int,
    val canSeeWhoLikedYou: Boolean,
    val canUseBoost: Boolean,
)
