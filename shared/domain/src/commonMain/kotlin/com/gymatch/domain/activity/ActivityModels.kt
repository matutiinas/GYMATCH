package com.gymatch.domain.activity

import com.gymatch.domain.model.SportType

enum class ExternalProvider { STRAVA, GARMIN, APPLE_HEALTH }

enum class VerificationStatus { PENDING, VERIFIED, REJECTED }

data class ExternalConnection(
    val userId: String,
    val provider: ExternalProvider,
    val externalAthleteId: String,
    val linkedAtEpochSec: Long,
    val isActive: Boolean,
)

data class VerifiedWorkout(
    val workoutId: String,
    val userId: String,
    val provider: ExternalProvider,
    val sportType: SportType,
    val cityCode: String,
    val categoryCode: String,
    val startedAtEpochSec: Long,
    val durationSec: Int,
    val distanceMeters: Double?,
    val calories: Int?,
    val verificationStatus: VerificationStatus,
)
