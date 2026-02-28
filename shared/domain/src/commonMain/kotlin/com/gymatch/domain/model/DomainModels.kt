package com.gymatch.domain.model

enum class SportType { GYM, RUNNING, FUNCTIONAL, OTHER }

enum class PresenceTag { ONLINE, RECENTLY_ACTIVE, NEW }

data class GeoPoint(val lat: Double, val lon: Double)

data class AthleteProfile(
    val userId: String,
    val sports: Set<SportType>,
    val level: String,
    val objectives: List<String>,
    val personalBests: Map<SportType, String>,
)

data class DiscoverCandidate(
    val userId: String,
    val displayName: String,
    val sports: Set<SportType>,
    val tags: Set<PresenceTag>,
)

data class SwipeAction(
    val fromUserId: String,
    val toUserId: String,
    val liked: Boolean,
)

data class MatchResult(
    val isMatch: Boolean,
    val matchId: String?,
)
